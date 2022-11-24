package cn.bixin.sona.gateway.task;

import cn.bixin.sona.gateway.cat.MonitorUtils;
import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.channel.handler.IdleChannelHandler;
import cn.bixin.sona.gateway.concurrent.counter.SystemClock;
import cn.bixin.sona.gateway.util.EventRecordLog;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qinwei
 */
@Slf4j
public class HeartbeatTimerTask extends ScheduleTimerTask {

    private final int idleTimeout;

    public HeartbeatTimerTask(long tick, int idleTimeout) {
        super(tick);
        this.idleTimeout = idleTimeout;
    }

    @Override
    protected void doTask(NettyChannel channel) {
        try {
            if (!channel.isConnected()) {
                return;
            }
            long now = SystemClock.currentTimeMillis();
            boolean isReadTimeout = isReadTimeout(channel, now);
            boolean isWriteTimeout = isWriteTimeout(channel, now);
            if (isReadTimeout || isWriteTimeout) {
                EventRecordLog.logEvent(channel, "Heartbeat timeout", idleTimeout + " ms");
                channel.close();
                MonitorUtils.logCatEventWithChannelAttrs(MonitorUtils.IDLE_STATE_EVENT, isReadTimeout ? "READER_IDLE" : "WRITER_IDLE", channel, true);
            }
        } catch (Throwable t) {
            log.warn("Exception when close channel " + channel.getRemoteAddress(), t);
        }
    }

    protected boolean isReadTimeout(NettyChannel channel, long now) {
        Long lastRead = lastRead(channel);
        return lastRead != null && now - lastRead > idleTimeout;
    }

    protected boolean isWriteTimeout(NettyChannel channel, long now) {
        Long lastWrite = lastWrite(channel);
        return lastWrite != null && now - lastWrite > idleTimeout;
    }

    public static Long lastRead(NettyChannel channel) {
        return channel.getAttribute(IdleChannelHandler.KEY_READ_TIMESTAMP, Long.class);
    }

    public static Long lastWrite(NettyChannel channel) {
        return channel.getAttribute(IdleChannelHandler.KEY_WRITE_TIMESTAMP, Long.class);
    }
}
