package cn.bixin.sona.gateway.concurrent.buffer;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;

/**
 * @author qinwei
 * <p>
 * 无锁化 ring buffer
 * <p>
 * buffer size 必须是2的幂次方 ，这样可以通过 位运算来计算下标，提升性能
 * <p>
 * index是long类型，即使100万QPS的处理速度，也需要30万年才能用完 ，disruptor 就是这样设计的
 * <p>
 * disruptor 里面的buffer 设计很牛逼，但是比较适合buffer 较大、单 producer 的场景，在多 producer 的情况下 write 会比较复杂，性能较差
 * 它里面会引入了一个与Ring Buffer 大小相同的 buffer：available buffer。
 * 当某个位置写入成功的时候，便把 availble buffer 相应的位置置位，标记为写入成功。
 * 读取的时候，会遍历 available buffer ，来判断元素是否已经就绪。
 * <p>
 * 这里我主要用于缓存房间消息，buffer size不会超过16，并且属于多 producer 的场景，如果使用 disruptor那种方式，性能不见得会高多少
 * 所以这里借鉴了 caffeine 里面的buffer 设计 ，实现比较简单，在buffer size 较小、并发 write 的情况下，能提供不错的性能
 * <p>
 * 借鉴 skywalking 中 解决伪共享的高性能设计
 * https://github.com/apache/skywalking/pull/2930
 */
public class RingBuffer<E> {

    private static final int FULL = -1;
    private static final int FAILED = 0;
    private static final int SUCCESS = 1;

    private static final int VALUE_OFFSET = 7;

    private final int bufferSize;

    private final int mask;

    private final AtomicReferenceArray<E> buffer;

    private final AtomicLongArray writeCounter;

    private final AtomicLongArray readCounter;

    /**
     * buffer size 必须是 2的幂次方
     */
    public RingBuffer(int bufferSize) {
        Assert.isTrue((bufferSize & (bufferSize - 1)) == 0, "buffer size not a power of two");
        this.buffer = new AtomicReferenceArray<>(bufferSize);
        this.bufferSize = bufferSize;
        this.mask = bufferSize - 1;
        this.writeCounter = new AtomicLongArray(15);
        this.writeCounter.set(VALUE_OFFSET, 0);
        this.readCounter = new AtomicLongArray(15);
        this.readCounter.set(VALUE_OFFSET, 0);
    }

    public final int offer(final E data) {
        long head = reads();
        long tail = writes();
        long size = tail - head;
        if (size >= bufferSize) {
            return FULL;
        }
        if (this.writeCounter.compareAndSet(VALUE_OFFSET, tail, tail + 1)) {
            int index = calculateIndex(tail);
            buffer.lazySet(index, data);
            return SUCCESS;
        }
        return FAILED;
    }

    public List<E> obtain() {
        long head = reads();
        long tail = writes();
        long size = tail - head;
        if (size == 0) {
            return null;
        }
        List<E> list = new ArrayList<>((int) size);
        do {
            int index = calculateIndex(head);
            list.add(buffer.get(index));
            buffer.lazySet(index, null);
            head++;
        } while (head != tail);
        lazySetReadCounter(head);
        return list;
    }

    public void offer(E data, Consumer<E> consumer) {
        for (int i = 0; ; i++) {
            int result = offer(data);
            if (result == SUCCESS) {
                return;
            }
            if (result == FULL) {
                drainTo(consumer);
            }
            waitFor(i);
        }
    }

    public void drainTo(Consumer<E> consumer) {
        long head = reads();
        long tail = writes();
        long size = tail - head;
        if (size == 0) {
            return;
        }
        do {
            int index = calculateIndex(head);
            E e = buffer.get(index);
            if (e == null) {
                break;
            }
            buffer.lazySet(index, null);
            consumer.accept(e);
            head++;
        } while (head != tail);
        lazySetReadCounter(head);
    }

    private void waitFor(int count) {
        if (count > 10) {
            LockSupport.parkNanos(1L);
        } else {
            Thread.yield();
        }
    }

    private int calculateIndex(long sequence) {
        return (int) (sequence & mask);
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public final long writes() {
        return this.writeCounter.get(VALUE_OFFSET);
    }

    public final long reads() {
        return this.readCounter.get(VALUE_OFFSET);
    }

    private void lazySetReadCounter(long head) {
        this.readCounter.lazySet(VALUE_OFFSET, head);
    }

}
