package cn.bixin.sona.gateway.channel.handler;

import cn.bixin.sona.common.spring.SpringApplicationContext;
import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.channel.support.NettyFuture;
import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.config.ApolloConfiguration;
import cn.bixin.sona.gateway.exception.RemoteException;
import cn.bixin.sona.gateway.task.HandshakeTimeoutTask;
import cn.bixin.sona.gateway.task.HeartbeatTimerTask;
import cn.bixin.sona.gateway.task.ProbeIdleTimerTask;
import cn.bixin.sona.gateway.util.AccessMessageUtils;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.NamedThreadFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author qinwei
 */
@Slf4j
public class IdleChannelHandler extends AbstractChannelHandler {

    public static final HashedWheelTimer IDLE_CHECK_TIMER = new HashedWheelTimer(new NamedThreadFactory("mercury-idleCheck", true), 1, TimeUnit.SECONDS, 128);

    public static final String KEY_READ_TIMESTAMP = "READ_TIMESTAMP";

    public static final String KEY_WRITE_TIMESTAMP = "WRITE_TIMESTAMP";

    public static final String KEY_HAND_SHAKE = "HAND_SHAKE_TIMER";

    public IdleChannelHandler(ChannelHandler handler) {
        super(handler);
        startIdleTask();
    }

    @Override
    public void connect(NettyChannel channel) throws RemoteException {
        setReadTimestamp(channel);
        setWriteTimestamp(channel);
        handler.connect(channel);
        startHandshakeTask(channel);
    }

    @Override
    public void disconnect(NettyChannel channel) throws RemoteException {
        clearReadTimestamp(channel);
        clearWriteTimestamp(channel);
        handler.disconnect(channel);
        closeTask(channel);
    }

    @Override
    public void send(NettyChannel channel, Object message) throws RemoteException {
        setWriteTimestamp(channel);
        handler.send(channel, message);
    }

    @Override
    public void receive(NettyChannel channel, Object message) throws RemoteException {
        setReadTimestamp(channel);
        AccessMessage msg = (AccessMessage) message;
        if (!msg.isHeartbeat()) {
            handler.receive(channel, message);
            return;
        }
        if (!msg.isReq()) {
            NettyFuture.received(msg, channel);
            return;
        }
        if (msg.isTwoWay()) {
            channel.send(AccessMessageUtils.createHeartResponse(msg.getId()));
        }
    }

    private static void setReadTimestamp(NettyChannel channel) {
        channel.setAttribute(KEY_READ_TIMESTAMP, System.currentTimeMillis());
    }

    private static void setWriteTimestamp(NettyChannel channel) {
        channel.setAttribute(KEY_WRITE_TIMESTAMP, System.currentTimeMillis());
    }

    private static void clearReadTimestamp(NettyChannel channel) {
        channel.removeAttribute(KEY_READ_TIMESTAMP);
    }

    private static void clearWriteTimestamp(NettyChannel channel) {
        channel.removeAttribute(KEY_WRITE_TIMESTAMP);
    }

    private static void startIdleTask() {
        ApolloConfiguration apolloConfig = SpringApplicationContext.getBean(ApolloConfiguration.class);
        //心跳检测
        int channelIdleSeconds = apolloConfig.getChannelIdleSeconds();
        HeartbeatTimerTask heartbeatTimerTask = new HeartbeatTimerTask(30 * 1000, channelIdleSeconds * 1000);
        IDLE_CHECK_TIMER.newTimeout(heartbeatTimerTask, 10, TimeUnit.SECONDS);
        //消息探测
        int probeIdleSeconds = apolloConfig.getProbeIdleSeconds();
        ProbeIdleTimerTask probeIdleTimerTask = new ProbeIdleTimerTask(30 * 1000, probeIdleSeconds * 1000);
        IDLE_CHECK_TIMER.newTimeout(probeIdleTimerTask, 10, TimeUnit.SECONDS);
    }

    private static void startHandshakeTask(NettyChannel channel) {
        //握手认证
        ApolloConfiguration apolloConfig = SpringApplicationContext.getBean(ApolloConfiguration.class);
        int handshakeWaitSeconds = apolloConfig.getHandshakeWaitSeconds();
        if (handshakeWaitSeconds > 0) {
            HandshakeTimeoutTask handshakeTimeoutTask = new HandshakeTimeoutTask(channel);
            channel.setAttribute(KEY_HAND_SHAKE, IDLE_CHECK_TIMER.newTimeout(handshakeTimeoutTask, handshakeWaitSeconds, TimeUnit.SECONDS));
        }
    }

    private static void closeTask(NettyChannel channel) {
        Timeout timeout = channel.removeAttribute(KEY_HAND_SHAKE, Timeout.class);
        if (timeout != null) {
            timeout.cancel();
        }
    }
}
