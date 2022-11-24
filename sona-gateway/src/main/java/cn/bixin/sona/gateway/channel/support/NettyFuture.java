package cn.bixin.sona.gateway.channel.support;

import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.common.AccessMessage;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.NamedThreadFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author qinwei
 */
@Slf4j
public class NettyFuture extends CompletableFuture<AccessMessage> {

    public static final Timer TIME_OUT_TIMER = new HashedWheelTimer(new NamedThreadFactory("future-timeout", true), 30, TimeUnit.MILLISECONDS);

    private final int id;

    private final int timeout;

    private final NettyChannel channel;

    private Timeout timeoutCheckTask;

    public NettyFuture(int id, int timeout, NettyChannel channel) {
        this.id = id;
        this.timeout = timeout;
        this.channel = channel;
        channel.getFutures().put(id, this);
    }

    public static NettyFuture newFuture(int id, int timeout, NettyChannel channel) {
        final NettyFuture future = new NettyFuture(id, timeout, channel);
        timeoutCheck(future);
        return future;
    }

    private static void timeoutCheck(NettyFuture future) {
        TimeoutCheckTask task = new TimeoutCheckTask(future.channel, future.id);
        future.timeoutCheckTask = TIME_OUT_TIMER.newTimeout(task, future.timeout, TimeUnit.MILLISECONDS);
    }

    public static void received(AccessMessage message, NettyChannel channel) {
        NettyFuture future = channel.getFutures().remove(message.getId());
        if (future == null) {
            return;
        }
        Timeout t = future.timeoutCheckTask;
        t.cancel();
        future.complete(message);
    }

    public static void exception(Throwable throwable, NettyChannel channel, int id) {
        NettyFuture future = channel.getFutures().remove(id);
        if (future == null) {
            return;
        }
        Timeout t = future.timeoutCheckTask;
        t.cancel();
        future.completeExceptionally(throwable);
    }

    public void cancel(Exception e) {
        exception(e, channel, id);
    }

    private static class TimeoutCheckTask implements TimerTask {

        private final NettyChannel channel;

        private final int id;

        TimeoutCheckTask(NettyChannel channel, int id) {
            this.channel = channel;
            this.id = id;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            NettyFuture future = channel.getFutures().get(id);
            if (future == null || future.isDone()) {
                return;
            }
            exception(new TimeoutException("request future has been timeout!"), channel, id);
        }
    }
}
