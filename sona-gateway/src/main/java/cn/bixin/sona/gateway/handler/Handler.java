package cn.bixin.sona.gateway.handler;


import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.exception.RemoteException;

/**
 * @author qinwei
 * <p>
 * 具体的业务处理，所有的业务处理类统一实现此接口
 */
public interface Handler {

    Object handle(NettyChannel channel, AccessMessage message) throws RemoteException;

    default void receive(NettyChannel channel, AccessMessage message) throws RemoteException {
        handle(channel, message);
    }

}
