package cn.bixin.sona.gateway.concurrent;

import org.apache.dubbo.common.utils.NamedThreadFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * * 把生产者队列、消费者队列分开，用两个锁去控制同步
 * * 当 consumer queue 为空时，且 producer queue 不为空条件满足时，会交换两个队列
 *
 * @author qinwei
 */
public class FastThreadPool {

    private static final int DEFAULT_QUEUE_SIZE = 1024;

    private BoundedQueue<Runnable> produced;
    private BoundedQueue<Runnable> toConsume;

    private final Lock consumerLock = new ReentrantLock();
    private final Lock producerLock = new ReentrantLock();
    private final Condition notFullCondition = producerLock.newCondition();
    private final Condition notEmptyCondition = producerLock.newCondition();

    private final List<Thread> threads;

    private volatile boolean stopped;

    public FastThreadPool(String name, int threadNum) {
        this(name, threadNum, 0);
    }

    public FastThreadPool(String name, int threadNum, int queueSize) {
        Assert.isTrue(threadNum > 0, "initialThreadNum=" + threadNum + " should be positive");
        threads = new ArrayList<>(threadNum);
        if (queueSize <= 0) {
            queueSize = DEFAULT_QUEUE_SIZE;
        }
        produced = new BoundedQueue<>(queueSize);
        toConsume = new BoundedQueue<>(queueSize);
        NamedThreadFactory threadFactory = new NamedThreadFactory(name, true);
        for (int i = 0; i < threadNum; i++) {
            Thread thread = threadFactory.newThread(this::consume);
            thread.start();
            threads.add(thread);
        }
    }

    private void consume() {
        Runnable task = null;
        while (true) {
            while (true) {
                consumerLock.lock();
                try {
                    if (!toConsume.isEmpty()) {
                        task = toConsume.pop();
                        break;
                    }
                } finally {
                    consumerLock.unlock();
                }
                producerLock.lock();
                try {
                    while (!stopped && produced.isEmpty()) {
                        try {
                            notEmptyCondition.await();
                        } catch (InterruptedException ignored) {
                        }
                    }
                    if (!produced.isEmpty()) {
                        if (produced.isFull()) {
                            notFullCondition.signalAll();
                        }
                        consumerLock.lock();
                        try {
                            BoundedQueue<Runnable> tmp = produced;
                            produced = toConsume;
                            toConsume = tmp;
                        } finally {
                            consumerLock.unlock();
                        }
                    } else {
                        break;
                    }
                } finally {
                    producerLock.unlock();
                }
            }
            if (task != null) {
                task.run();
            } else {
                break;
            }
        }
    }

    public void stop() {
        stopped = true;
        producerLock.lock();
        try {
            notEmptyCondition.signalAll();
            notFullCondition.signalAll();
        } finally {
            producerLock.unlock();
        }
    }

    public boolean submit(Runnable task) {
        Runnable[] tasks = {task};
        return submit(tasks, 0, 1) == 1;
    }

    public int submit(Runnable[] tasks, int offset, int len) {
        int cur = offset;
        int end = offset + len;
        while (!stopped && cur < end) {
            producerLock.lock();
            try {
                while (produced.isFull()) {
                    try {
                        notFullCondition.await();
                    } catch (InterruptedException ignored) {
                    }
                }
                int toProduce = Math.min(produced.remainingCapacity(), end - cur);
                if (toProduce > 0) {
                    boolean wasEmpty = produced.isEmpty();
                    produced.addAll(tasks, cur, toProduce);
                    if (wasEmpty) {
                        notEmptyCondition.signalAll();
                    }
                }
                cur += toProduce;
            } finally {
                producerLock.unlock();
            }
        }
        return cur - offset;
    }

    public boolean isStopped() {
        return stopped;
    }

}
