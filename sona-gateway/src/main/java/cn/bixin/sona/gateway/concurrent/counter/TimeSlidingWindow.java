package cn.bixin.sona.gateway.concurrent.counter;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author qinwei
 * <p>
 * 这里时间窗使用 LongAdder 计数 ，它 的 add 相比 AtomicLong或者其他类似的，在高并发情况下可以减少竞争，避免长时间自旋消耗cpu
 */
public class TimeSlidingWindow extends SlidingWindow<LongAdder> {

    private final int threshold;

    public TimeSlidingWindow(int threshold) {
        super();
        this.threshold = threshold;
    }

    public void increment() {
        add(1);
    }

    public void add(int x) {
        currentWindow().value().add(x);
    }

    public boolean exceedThreshold() {
        long total = values().stream().mapToLong(LongAdder::sum).sum();
        return total >= threshold;
    }

    @Override
    protected LongAdder newEmptyBucket(long timeMillis) {
        return new LongAdder();
    }

    @Override
    protected WindowHolder<LongAdder> resetWindowTo(WindowHolder<LongAdder> windowHolder, long startTime) {
        windowHolder.resetTo(startTime);
        windowHolder.value().reset();
        return windowHolder;
    }
}
