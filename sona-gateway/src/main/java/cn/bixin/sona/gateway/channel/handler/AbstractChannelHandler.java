package cn.bixin.sona.gateway.channel.handler;

import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.exception.RemoteException;
import org.springframework.util.Assert;

/**
 * @author qinwei
 */
public abstract class AbstractChannelHandler implements ChannelHandler {

    protected final ChannelHandler handler;

    public AbstractChannelHandler(ChannelHandler handler) {
        Assert.notNull(handler, "handler == null");
        this.handler = handler;
    }

    @Override
    public void connect(NettyChannel channel) throws RemoteException {
        handler.connect(channel);
    }

    @Override
    public void disconnect(NettyChannel channel) throws RemoteException {
        handler.disconnect(channel);
    }

    @Override
    public void send(NettyChannel channel, Object message) throws RemoteException {
        handler.send(channel, message);
    }

    @Override
    public void receive(NettyChannel channel, Object message) throws RemoteException {
        handler.receive(channel, message);
    }

    @Override
    public void caught(NettyChannel channel, Throwable exception) throws RemoteException {
        handler.caught(channel, exception);
    }

}
