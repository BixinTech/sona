package cn.bixin.sona.gateway.concurrent.counter;

/**
 * @author qinwei
 */
public class WindowHolder<T> {

    private final long windowLengthInMs;

    private long windowStart;

    private T value;

    public WindowHolder(long windowLengthInMs, long windowStart, T value) {
        this.windowLengthInMs = windowLengthInMs;
        this.windowStart = windowStart;
        this.value = value;
    }

    public long windowLength() {
        return windowLengthInMs;
    }

    public long windowStart() {
        return windowStart;
    }

    public T value() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    /**
     * 复位该时间窗口
     */
    public void resetTo(long startTime) {
        this.windowStart = startTime;
    }

    /**
     * 判断是否该时间在该窗口内
     */
    public boolean isTimeInWindow(long timeMillis) {
        return windowStart <= timeMillis && timeMillis < windowStart + windowLengthInMs;
    }

}
