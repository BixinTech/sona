package cn.bixin.sona.gateway.concurrent.counter;

import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author qinwei
 * <p>
 * thread safe ，精确的滑动窗口计数，主要借鉴了 sentinel 中的设计。
 * <p>
 * 时间越精细，空间消耗越大，总的桶个数最好控制在 10 以内，总的时间长度不要低于 1s
 * <p>
 * Hystrix里面是一个滑动窗口包含10个桶（Bucket），每个桶时间宽度是1秒，负责1秒的数据统计
 * <p>
 * 这里主要用于房间消息频率统计，所以我设的默认总的时间长度是1s，滑动窗口包含10个桶，就相当于是计算房间 qps 了
 */
public abstract class SlidingWindow<T> {

    /**
     * 单位时间窗口长度
     */
    private final int windowLength;

    /**
     * 总的时间长度
     */
    private final int timeSpan;

    private final AtomicReferenceArray<WindowHolder<T>> array;

    private final ReentrantLock updateLock = new ReentrantLock();

    public SlidingWindow() {
        this(10, 1000);
    }

    public SlidingWindow(int bucketCount, int timeSpan) {
        Assert.isTrue(timeSpan % bucketCount == 0, "time span needs to be evenly divided");
        this.windowLength = timeSpan / bucketCount;
        this.timeSpan = timeSpan;
        this.array = new AtomicReferenceArray<>(bucketCount);
    }

    public WindowHolder<T> currentWindow() {
        return currentWindow(SystemClock.currentTimeMillis());
    }

    private int calculateTimeIdx(long timeMillis) {
        long timeId = timeMillis / windowLength;
        return (int) (timeId % array.length());
    }

    protected long calculateWindowStart(long timeMillis) {
        return timeMillis - timeMillis % windowLength;
    }

    private WindowHolder<T> currentWindow(long timeMillis) {
        int idx = calculateTimeIdx(timeMillis);
        long windowStart = calculateWindowStart(timeMillis);
        while (true) {
            WindowHolder<T> old = array.get(idx);
            if (old == null) {
                WindowHolder<T> window = new WindowHolder<>(windowLength, windowStart, newEmptyBucket(timeMillis));
                if (array.compareAndSet(idx, null, window)) {
                    return window;
                }
                Thread.yield();
            } else if (windowStart == old.windowStart()) {
                return old;
            } else if (windowStart > old.windowStart()) {
                if (updateLock.tryLock()) {
                    try {
                        return resetWindowTo(old, windowStart);
                    } finally {
                        updateLock.unlock();
                    }
                }
                Thread.yield();
            } else if (windowStart < old.windowStart()) {
                // Should not go through here
                return new WindowHolder<>(windowLength, windowStart, newEmptyBucket(timeMillis));
            }
        }
    }

    private WindowHolder<T> getPreviousWindow(long timeMillis) {
        int idx = calculateTimeIdx(timeMillis - windowLength);
        timeMillis = timeMillis - windowLength;
        WindowHolder<T> wrap = array.get(idx);
        if (wrap == null || isWindowDeprecated(wrap)) {
            return null;
        }
        if (wrap.windowStart() + windowLength < (timeMillis)) {
            return null;
        }
        return wrap;
    }

    public WindowHolder<T> getPreviousWindow() {
        return getPreviousWindow(SystemClock.currentTimeMillis());
    }

    public T getWindowValue(long timeMillis) {
        int idx = calculateTimeIdx(timeMillis);
        WindowHolder<T> bucket = array.get(idx);
        if (bucket == null || !bucket.isTimeInWindow(timeMillis)) {
            return null;
        }
        return bucket.value();
    }

    private boolean isWindowDeprecated(WindowHolder<T> windowHolder) {
        return isWindowDeprecated(SystemClock.currentTimeMillis(), windowHolder);
    }

    private boolean isWindowDeprecated(long time, WindowHolder<T> windowHolder) {
        return time - windowHolder.windowStart() > timeSpan;
    }

    public List<WindowHolder<T>> list() {
        return list(SystemClock.currentTimeMillis());
    }

    private List<WindowHolder<T>> list(long validTime) {
        return IntStream.range(0, array.length()).mapToObj(array::get).filter(windowHolder -> windowHolder != null && !isWindowDeprecated(validTime, windowHolder)).collect(Collectors.toList());
    }

    public List<WindowHolder<T>> listAll() {
        return IntStream.range(0, array.length()).mapToObj(array::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<T> values() {
        return values(SystemClock.currentTimeMillis());
    }

    private List<T> values(long timeMillis) {
        return IntStream.range(0, array.length()).mapToObj(array::get).filter(windowHolder -> windowHolder != null && !isWindowDeprecated(timeMillis, windowHolder)).map(WindowHolder::value).collect(Collectors.toList());
    }

    protected abstract T newEmptyBucket(long timeMillis);

    protected abstract WindowHolder<T> resetWindowTo(WindowHolder<T> windowHolder, long startTime);
}
