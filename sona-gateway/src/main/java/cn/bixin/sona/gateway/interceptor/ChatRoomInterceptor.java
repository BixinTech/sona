package cn.bixin.sona.gateway.interceptor;

import cn.bixin.sona.gateway.cat.MonitorUtils;
import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.channel.support.ChannelAttrs;
import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.common.ChannelTypeEnum;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author qinwei
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Interceptor(name = "chatRoom")
public class ChatRoomInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(NettyChannel channel, AccessMessage message) throws Exception {
        ChannelAttrs attrs = channel.getAttrs();
        if (ChannelTypeEnum.CHATROOM.getType() != attrs.getChannelType()) {
            MonitorUtils.logEvent(MonitorUtils.CHATROOM_PROBLEM, "WrongChannelType");
            return false;
        }
        return true;
    }

}
