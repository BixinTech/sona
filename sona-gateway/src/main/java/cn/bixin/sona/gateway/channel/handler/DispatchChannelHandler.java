package cn.bixin.sona.gateway.channel.handler;

import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.channel.support.ChannelEventState;
import cn.bixin.sona.gateway.channel.support.ChannelEventTask;
import cn.bixin.sona.gateway.concurrent.OrderedChannelExecutor;
import cn.bixin.sona.gateway.exception.RemoteException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author qinwei
 * <p>
 * handler分发，通过线程池并发执行
 */
@Slf4j
public class DispatchChannelHandler extends AbstractChannelHandler {

    public static final ThreadPoolExecutor SHARED_EXECUTOR = new OrderedChannelExecutor(64, "mercury-handler-");

    public DispatchChannelHandler(ChannelHandler handler) {
        super(handler);
    }

    @Override
    public void connect(NettyChannel channel) throws RemoteException {
        try {
            SHARED_EXECUTOR.execute(ChannelEventTask.newInstance(handler, channel, ChannelEventState.CONNECT));
        } catch (Throwable t) {
            throw new RemoteException("connect event error , channel : " + channel + ".", t);
        }
    }

    @Override
    public void disconnect(NettyChannel channel) throws RemoteException {
        try {
            SHARED_EXECUTOR.execute(ChannelEventTask.newInstance(handler, channel, ChannelEventState.DISCONNECT));
        } catch (Throwable t) {
            throw new RemoteException("disconnect event error , channel : " + channel + ".", t);
        }
    }

    @Override
    public void receive(NettyChannel channel, Object message) throws RemoteException {
        try {
            SHARED_EXECUTOR.execute(ChannelEventTask.newInstance(handler, channel, ChannelEventState.RECEIVE, message));
        } catch (Throwable t) {
            throw new RemoteException("receive event error , channel : " + channel + ".", t, message);
        }
    }

    @Override
    public void send(NettyChannel channel, Object message) throws RemoteException {
        try {
            SHARED_EXECUTOR.execute(ChannelEventTask.newInstance(handler, channel, ChannelEventState.SENT, message));
        } catch (Throwable t) {
            throw new RemoteException("send event error , channel : " + channel + ".", t);
        }
    }

    @Override
    public void caught(NettyChannel channel, Throwable exception) throws RemoteException {
        try {
            SHARED_EXECUTOR.execute(ChannelEventTask.newInstance(handler, channel, ChannelEventState.CAUGHT, exception));
        } catch (Throwable t) {
            throw new RemoteException("caught event error , channel : " + channel + ".", t);
        }
    }
}
