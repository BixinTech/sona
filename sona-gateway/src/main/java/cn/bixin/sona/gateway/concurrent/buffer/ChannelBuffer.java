package cn.bixin.sona.gateway.concurrent.buffer;

import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.common.BatchResolve;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.function.Consumer;

/**
 * @author qinwei
 * <p>
 * thread safe
 * <p>
 * buffer size 必须是2的幂次方，这里默认16 ，如果是8的话，太容易迅速被填满了，可能会导致性能不高
 * 而且有sona那边的频控，还有每隔50ms 我都会把buffer里面的数据拿出来合并发送，一般也不会达到16
 */
public class ChannelBuffer extends RingBuffer<AccessMessage> {

    private static final int DEFAULT_BUFFER_SIZE = 16;

    /**
     * 减少内存使用
     */
    private static final AtomicIntegerFieldUpdater<ChannelBuffer> STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(ChannelBuffer.class, "state");

    private volatile int state;

    public ChannelBuffer() {
        super(DEFAULT_BUFFER_SIZE);
    }

    @Override
    public void drainTo(Consumer<AccessMessage> consumer) {
        if (STATE_UPDATER.compareAndSet(this, 0, 1)) {
            try {
                batchConsumer(obtain(), consumer);
            } finally {
                STATE_UPDATER.set(this, 0);
            }
        }
    }

    private static void batchConsumer(List<AccessMessage> list, Consumer<AccessMessage> consumer) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        consumer.accept(BatchResolve.merge(list));
    }

}
