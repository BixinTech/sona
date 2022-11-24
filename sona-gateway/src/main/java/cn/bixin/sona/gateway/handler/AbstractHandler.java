package cn.bixin.sona.gateway.handler;

import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.exception.RemoteException;

/**
 * @author qinwei
 */
public abstract class AbstractHandler implements Handler {

    @Override
    public Object handle(NettyChannel channel, AccessMessage message) throws RemoteException {
        // 这里可以加一些通用处理
        return doHandle(channel, message);
    }

    protected abstract Object doHandle(NettyChannel channel, AccessMessage message) throws RemoteException;
}
