package cn.bixin.sona.gateway.interceptor;

import cn.bixin.sona.gateway.cat.MonitorUtils;
import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.channel.handler.IdleChannelHandler;
import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.common.CommandEnum;
import cn.bixin.sona.gateway.handler.LoginAuthHandler;
import cn.bixin.sona.gateway.msg.AccessResponse;
import cn.bixin.sona.gateway.util.AccessMessageUtils;
import cn.bixin.sona.gateway.util.EventRecordLog;
import com.alibaba.fastjson.JSON;
import io.netty.util.Timeout;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author qinwei
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Interceptor(name = "loginAuth")
public class LoginAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(NettyChannel channel, AccessMessage message) throws Exception {
        byte[] body = message.getBody();
        if (body == null || body.length == 0) {
            channel.send(AccessMessageUtils.createResponse(message.getId(), message.getCmd(), JSON.toJSONBytes(AccessResponse.ACCESS_FAIL)), false, true);
            EventRecordLog.logEvent(channel, LoginAuthHandler.LOGIN_EVENT, message, "EmptyBody");
            MonitorUtils.logEvent(MonitorUtils.LOGIN_PROBLEM, "EmptyBody");
            return false;
        }
        if (CommandEnum.LOGIN_AUTH.getCommand() != message.getCmd()) {
            EventRecordLog.logEvent(channel, LoginAuthHandler.LOGIN_EVENT, message, "Unknown cmd");
            MonitorUtils.logEvent(MonitorUtils.LOGIN_PROBLEM, "UnknownCmd");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(NettyChannel channel, AccessMessage message) throws Exception {
        Timeout timeout = channel.removeAttribute(IdleChannelHandler.KEY_HAND_SHAKE, Timeout.class);
        if (timeout != null) {
            timeout.cancel();
        }
    }

}
