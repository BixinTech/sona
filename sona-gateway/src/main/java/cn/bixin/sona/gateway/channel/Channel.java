package cn.bixin.sona.gateway.channel;


import cn.bixin.sona.gateway.channel.support.NettyFuture;
import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.exception.RemoteException;

import java.net.InetSocketAddress;

/**
 * @author qinwei
 * <p>
 * 对 netty 底层 channel 的封装，业务层直接操作 NettyChannel，无需关心底层channel的处理
 */
public interface Channel {

    InetSocketAddress getLocalAddress();

    InetSocketAddress getRemoteAddress();

    boolean isConnected();

    NettyFuture request(AccessMessage request) throws RemoteException;

    NettyFuture request(AccessMessage request, int timeout) throws RemoteException;

    void send(Object message) throws RemoteException;

    void send(Object message, boolean sent, boolean closeWhenComplete) throws RemoteException;

    void close();

    boolean isClosed();

    boolean hasAttribute(String key);

    Object getAttribute(String key);

    void setAttribute(String key, Object value);

    Object removeAttribute(String key);
}
