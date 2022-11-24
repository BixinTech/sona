package cn.bixin.sona.gateway.task;

import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * @author qinwei
 */
@Getter
public abstract class AbstractTimerTask implements TimerTask {

    private final long tick;

    private int retry;

    private volatile boolean cancel;

    public AbstractTimerTask(long tick) {
        this.tick = tick;
    }

    public void cancel() {
        this.cancel = true;
    }

    protected void retry(Timeout timeout) {
        retry(timeout, tick);
    }

    protected void retry(Timeout timeout, long tick) {
        Timer timer = getTimer(timeout);
        if (timer == null) {
            return;
        }
        retry++;
        timer.newTimeout(this, tick, TimeUnit.MILLISECONDS);
    }

    protected void reput(Timeout timeout) {
        reput(timeout, tick, this);
    }

    protected void reput(Timeout timeout, long tick) {
        reput(timeout, tick, this);
    }

    protected void reput(Timeout timeout, long tick, TimerTask task) {
        Timer timer = getTimer(timeout);
        if (timer == null) {
            return;
        }
        timer.newTimeout(task, tick, TimeUnit.MILLISECONDS);
    }

    private Timer getTimer(Timeout timeout) {
        if (timeout == null) {
            return null;
        }
        if (cancel) {
            return null;
        }
        Timer timer = timeout.timer();
        if (timer == null || timeout.isCancelled()) {
            return null;
        }
        return timer;
    }

}
