package cn.bixin.sona.gateway.channel.handler;

import cn.bixin.sona.common.spring.SpringApplicationContext;
import cn.bixin.sona.gateway.cat.MonitorUtils;
import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.channel.RoomChannelManager;
import cn.bixin.sona.gateway.channel.support.ChannelAttrs;
import cn.bixin.sona.gateway.common.ChannelTypeEnum;
import cn.bixin.sona.gateway.concurrent.counter.SystemClock;
import cn.bixin.sona.gateway.exception.AccessMessageDecodeException;
import cn.bixin.sona.gateway.exception.RemoteException;
import cn.bixin.sona.gateway.service.SocketNotifyService;
import cn.bixin.sona.gateway.util.EventRecordLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.util.CollectionUtils;

import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author qinwei
 */
@Slf4j
public class ChannelHandlerDelegate implements ChannelHandler {

    private static final Pattern IGNORABLE_ERROR_MESSAGE = Pattern.compile(
            "^.*(?:connection.*(?:reset|closed|abort|broken)|broken.*pipe).*$", Pattern.CASE_INSENSITIVE);

    @Override
    public void connect(NettyChannel channel) throws RemoteException {

    }

    @Override
    public void disconnect(NettyChannel channel) throws RemoteException {
        ChannelAttrs attrs = channel.getAttrsIfExists();
        if (attrs == null) {
            return;
        }
        double duration = (SystemClock.currentTimeMillis() - attrs.getCreateTime()) / 1000.0;
        int channelType = attrs.getChannelType();
        if (ChannelTypeEnum.CHATROOM.getType() == channelType) {
            Set<String> rooms = attrs.getRooms();
            if (!CollectionUtils.isEmpty(rooms)) {
                for (String roomName : rooms) {
                    RoomChannelManager.MANAGER_FOR_CHATROOM.removeChannel(roomName, channel);
                }
            }
        }
        if (channel.isAuth()) {
            SendResult result = SpringApplicationContext.getBean(SocketNotifyService.class).processDisConnect(channel);
            StringBuilder msg = new StringBuilder().append(duration).append("s.");
            if (result != null) {
                msg.append(result.getSendStatus()).append('=').append(result.getMsgId());
            }
            EventRecordLog.logEvent(channel, "TTL", msg.toString());
        } else {
            log.info("inactive channel, address={}, type={}, duration={} s", channel.getRemoteAddress(), channelType, duration);
        }
    }

    @Override
    public void send(NettyChannel channel, Object message) throws RemoteException {

    }

    @Override
    public void receive(NettyChannel channel, Object message) throws RemoteException {

    }

    @Override
    public void caught(NettyChannel channel, Throwable exception) throws RemoteException {
        InetSocketAddress remoteAddress = channel.getRemoteAddress();
        String message = exception.getMessage();
        // for epoll module
        if (ignoreException(message)) {
            if (message.contains("Connection reset by peer")) {
                MonitorUtils.logCatEventWithChannelAttrs(MonitorUtils.CONNECTION_RESET_BY_PEER, "", channel, true);
                return;
            }
            if (message.contains("Broken pipe")) {
                log.info("IOException.BrokenPipe, remoteAddress={}", remoteAddress);
                MonitorUtils.logCatEventWithChannelAttrs(MonitorUtils.SAFE_EXCEPTION, "BrokenPipe", channel, true, true);
            }
            return;
        }
        if (exception instanceof AccessMessageDecodeException || exception.getCause() instanceof AccessMessageDecodeException) {
            Throwable e = exception;
            while (e != null) {
                e = e.getCause();
            }
            log.error("AccessMessageDecodeException, remoteAddress = {}", remoteAddress, exception);
            return;
        }
        if (exception instanceof RemoteException) {
            RemoteException ex = (RemoteException) exception;
            log.warn("RemoteException , remoteAddress = {} , msg = {}", remoteAddress, ex.getMsgBody(), ex);
            return;
        }
        if (exception instanceof ClosedChannelException) {
            log.warn("channel close ! remoteAddress= {}", remoteAddress);
            return;
        }
        log.error("exceptionCaught, remoteAddress=" + remoteAddress, exception);
    }

    private static boolean ignoreException(String message) {
        return message != null && IGNORABLE_ERROR_MESSAGE.matcher(message).matches();
    }
}
