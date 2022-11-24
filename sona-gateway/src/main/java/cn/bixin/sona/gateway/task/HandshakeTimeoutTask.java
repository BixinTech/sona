package cn.bixin.sona.gateway.task;

import cn.bixin.sona.gateway.cat.MonitorUtils;
import cn.bixin.sona.gateway.channel.NettyChannel;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qinwei
 */
@Slf4j
public class HandshakeTimeoutTask implements TimerTask {

    private final NettyChannel channel;

    public HandshakeTimeoutTask(NettyChannel channel) {
        this.channel = channel;
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        try {
            if (!channel.isConnected()) {
                return;
            }
            channel.close();
            log.info("HandshakeNotReceived, remoteAddress={}", channel.getRemoteAddress());
            MonitorUtils.logEvent(MonitorUtils.LOGIN_PROBLEM, "HandshakeNotReceived");
        } catch (Throwable t) {
            log.warn("Exception when handle handshake task , channel " + channel.getRemoteAddress(), t);
        }
    }
}
