package cn.bixin.sona.gateway.task;

import io.netty.util.Timeout;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qinwei
 */
@Slf4j
public abstract class RetryTimerTask extends AbstractTimerTask {

    private int maxRetries;

    private int waitTime;

    public RetryTimerTask(int maxRetries) {
        this(maxRetries, 0);
    }

    public RetryTimerTask(int maxRetries, int waitTime) {
        this(0, maxRetries, waitTime);
    }

    public RetryTimerTask(long tick, int maxRetries, int waitTime) {
        super(tick);
        this.maxRetries = maxRetries;
        this.waitTime = waitTime;
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        try {
            doTask();
        } catch (Throwable e) {
            log.error("RetryTimerTask execute failure , the task will retry {} times after {} ms. ", maxRetries - getRetry(), waitTime, e);
            if (getRetry() < maxRetries) {
                retry(timeout, waitTime);
            }
        }
    }

    protected abstract void doTask();
}
