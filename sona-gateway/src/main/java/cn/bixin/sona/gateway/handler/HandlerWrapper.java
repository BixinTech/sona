package cn.bixin.sona.gateway.handler;

import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.exception.RemoteException;
import cn.bixin.sona.gateway.interceptor.HandlerInterceptorChain;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qinwei
 */
@Slf4j
public class HandlerWrapper implements Handler {

    private final Handler handler;

    private final HandlerInterceptorChain chain;

    public HandlerWrapper(String name, Handler handler) {
        this.handler = handler;
        this.chain = HandlerInterceptorChain.getHandlerInterceptorChain(name);
    }

    @Override
    public Object handle(NettyChannel channel, AccessMessage message) throws RemoteException {
        if (handler == null) {
            return null;
        }
        try {
            if (!chain.applyPreHandle(channel, message)) {
                return null;
            }
            Object result = handler.handle(channel, message);
            chain.applyPostHandle(channel, message);
            chain.applyAfterHandle(channel, message, null);
            return result;
        } catch (Exception e) {
            chain.applyAfterHandle(channel, message, e);
            throw new RemoteException("HandlerInterceptorChain execute failure !", e);
        }
    }
}
