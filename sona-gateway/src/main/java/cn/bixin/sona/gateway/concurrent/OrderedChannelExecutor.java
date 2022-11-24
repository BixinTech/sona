package cn.bixin.sona.gateway.concurrent;

import cn.bixin.sona.gateway.channel.support.ChannelEventTask;
import cn.bixin.sona.gateway.loadbalance.ConsistentHashLoadBalance;
import cn.bixin.sona.gateway.loadbalance.LoadBalance;
import cn.bixin.sona.gateway.util.NetUtil;
import io.netty.util.internal.PlatformDependent;
import net.openhft.affinity.AffinityStrategies;
import net.openhft.affinity.AffinityThreadFactory;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author qinwei
 * <p>
 * 自定义线程池，提升处理能力，并且可以保证单个 channel 的处理顺序
 */
public class OrderedChannelExecutor extends ThreadPoolExecutor {

    /**
     * 减少内存消耗
     */
    private static final AtomicIntegerFieldUpdater<SerialExecutor> STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(SerialExecutor.class, "state");

    private LoadBalance<Executor> loadBalance;

    public OrderedChannelExecutor(int poolSize, String name) {
        this(poolSize, poolSize, 0, TimeUnit.SECONDS, new SynchronousQueue<>(), new AffinityThreadFactory(name, AffinityStrategies.DIFFERENT_CORE), new DiscardPolicy());
    }

    public OrderedChannelExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        init();
    }

    private void init() {
        List<Executor> list = IntStream.range(0, 64).mapToObj(SerialExecutor::new).collect(Collectors.toList());
        loadBalance = new ConsistentHashLoadBalance<>(list);
    }

    @Override
    public void execute(Runnable command) {
        if (!(command instanceof ChannelEventTask)) {
            throw new RejectedExecutionException("command must be " + ChannelEventTask.class.getName() + "!");
        }
        ChannelEventTask task = (ChannelEventTask) command;
        getSerialExecutor(task).execute(command);
    }

    private void dispatch(Runnable task) {
        super.execute(task);
    }

    private Executor getSerialExecutor(ChannelEventTask task) {
        String channelId = task.getChannel().getChannelId();
        return loadBalance.selectNode(channelId);
    }

    private final class SerialExecutor implements Executor, Runnable {

        private final Queue<Runnable> tasks = PlatformDependent.newMpscQueue();

        public volatile int state;

        private final int sequence;

        SerialExecutor(int sequence) {
            this.sequence = sequence;
        }

        @Override
        public void execute(Runnable command) {
            tasks.add(command);

            if (STATE_UPDATER.get(this) == 0) {
                dispatch(this);
            }
        }

        @Override
        public void run() {
            if (STATE_UPDATER.compareAndSet(this, 0, 1)) {
                try {
                    Thread thread = Thread.currentThread();
                    for (; ; ) {
                        final Runnable task = tasks.poll();

                        if (task == null) {
                            break;
                        }

                        boolean ran = false;
                        beforeExecute(thread, task);
                        try {
                            task.run();
                            ran = true;
                            afterExecute(task, null);
                        } catch (Exception e) {
                            if (!ran) {
                                afterExecute(task, e);
                            }
                            throw e;
                        }
                    }
                } finally {
                    STATE_UPDATER.set(this, 0);
                }

                if (STATE_UPDATER.get(this) == 0 && tasks.peek() != null) {
                    dispatch(this);
                }
            }
        }

        @Override
        public String toString() {
            return NetUtil.LOCAL_IP_ADDR + "|" + sequence;
        }
    }

}
