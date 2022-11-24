package cn.bixin.sona.gateway.channel.handler;

import cn.bixin.sona.gateway.cat.MonitorUtils;
import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.exception.RemoteException;
import cn.bixin.sona.gateway.util.AccessMessageUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qinwei
 * <p>
 * cat 数据上报
 */
@Slf4j
public class CatReportChannelHandler extends AbstractChannelHandler {

    public CatReportChannelHandler(ChannelHandler handler) {
        super(handler);
    }

    @Override
    public void send(NettyChannel channel, Object message) throws RemoteException {
        if (!channel.isConnected()) {
            log.warn("ChannelNotActiveInHandler, channelId={}, remoteAddress={}, cmd={}",
                    channel.getChannelId(), channel.getRemoteAddress(), message instanceof AccessMessage ? ((AccessMessage) message).getCmd() : message.getClass().getSimpleName());
            MonitorUtils.logCatEventWithMessage(MonitorUtils.SEND_MESSAGE_PROBLEM, "ChannelNotActiveInHandler", message, false);
            return;
        }

        handler.send(channel, message);
    }

    @Override
    public void receive(NettyChannel channel, Object message) throws RemoteException {
        if (!(message instanceof AccessMessage)) {
            MonitorUtils.logEvent(MonitorUtils.RECEIVE_MESSAGE_PROBLEM, channel.getChannelId());
            log.error("Unsupport message :{} , remoteAddress={}", JSON.toJSONString(message), channel.getRemoteAddress());
            return;
        }
        AccessMessage msg = (AccessMessage) message;
        String name = msg.isHeartbeat() ? "HB" : String.valueOf(msg.getCmd());
        AccessMessageUtils.logInboundMsgSize(msg, name);
        handler.receive(channel, message);
    }
}
