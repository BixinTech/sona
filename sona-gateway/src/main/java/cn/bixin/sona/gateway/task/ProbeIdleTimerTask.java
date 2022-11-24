package cn.bixin.sona.gateway.task;

import cn.bixin.sona.common.spring.SpringApplicationContext;
import cn.bixin.sona.gateway.cat.MonitorUtils;
import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.channel.support.ChannelAttrs;
import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.concurrent.counter.SystemClock;
import cn.bixin.sona.gateway.config.ApolloConfiguration;
import cn.bixin.sona.gateway.util.AccessMessageUtils;
import cn.bixin.sona.gateway.util.EventRecordLog;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeoutException;

/**
 * @author qinwei
 */
@Slf4j
public class ProbeIdleTimerTask extends HeartbeatTimerTask {

    public ProbeIdleTimerTask(long tick, int idleTimeout) {
        super(tick, idleTimeout);
    }

    @Override
    protected void doTask(NettyChannel channel) {
        try {
            if (!channel.isConnected()) {
                return;
            }
            long now = SystemClock.currentTimeMillis();
            boolean isReadTimeout = isReadTimeout(channel, now);
            if (isReadTimeout) {
                ChannelAttrs attrs = channel.getAttrsIfExists();
                if (attrs == null) {
                    return;
                }
                if (attrs.isForeground()) {
                    int probeWaitSeconds = SpringApplicationContext.getBean(ApolloConfiguration.class).getProbeWaitSeconds();
                    if (probeWaitSeconds <= 0) {
                        return;
                    }
                    log.info("ProbeSent, remoteAddress={}", channel.getRemoteAddress());
                    AccessMessage request = AccessMessageUtils.createHeartRequest(channel.getSequece());
                    channel.request(request, probeWaitSeconds * 1000)
                            .whenComplete((message, throwable) -> {
                                if (throwable instanceof TimeoutException) {
                                    EventRecordLog.logEvent(channel, "Probe timeout", probeWaitSeconds + " s");
                                    channel.close();
                                    MonitorUtils.logCatEventWithChannelAttrs(MonitorUtils.PROBE_IDLE, "", channel, false);
                                }
                            });
                }
            }
        } catch (Throwable t) {
            log.warn("Exception when handle probe message , channel " + channel.getRemoteAddress(), t);
        }
    }
}
