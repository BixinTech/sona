package cn.bixin.sona.gateway.channel.handler;


import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.exception.RemoteException;

/**
 * @author qinwei
 * <p>
 * 对netty 底层事件的封装，业务层根据不同的事件执行相应的处理
 */
public interface ChannelHandler {

    void connect(NettyChannel channel) throws RemoteException;

    void disconnect(NettyChannel channel) throws RemoteException;

    void send(NettyChannel channel, Object message) throws RemoteException;

    void receive(NettyChannel channel, Object message) throws RemoteException;

    void caught(NettyChannel channel, Throwable exception) throws RemoteException;

}
