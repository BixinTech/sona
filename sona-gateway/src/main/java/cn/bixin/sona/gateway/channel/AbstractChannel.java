package cn.bixin.sona.gateway.channel;

import cn.bixin.sona.gateway.channel.support.NettyFuture;
import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.exception.RemoteException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qinwei
 */
public abstract class AbstractChannel implements Channel {

    private volatile boolean closed;

    private volatile boolean active;

    protected final Map<Integer, NettyFuture> futures = new ConcurrentHashMap<>();

    @Override
    public NettyFuture request(AccessMessage request) throws RemoteException {
        return request(request, 3000);
    }

    @Override
    public void send(Object message) throws RemoteException {
        send(message, false, false);
    }

    public void markActive(boolean isActive) {
        this.active = isActive;
    }

    public boolean isActive() {
        return this.active;
    }

    @Override
    public boolean isConnected() {
        return !isClosed() && isActive();
    }

    @Override
    public void send(Object message, boolean sent, boolean closeWhenComplete) throws RemoteException {
        if (isClosed()) {
            throw new RemoteException(this, "Failed to send message " + (message == null ? "" : message.getClass().getName()) + ":" + message
                    + ", cause: Channel closed. channel: " + getLocalAddress() + " -> " + getRemoteAddress());
        }
    }

    @Override
    public void close() {
        this.closed = true;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    public Map<Integer, NettyFuture> getFutures() {
        return futures;
    }

    @Override
    public String toString() {
        return getLocalAddress() + " -> " + getRemoteAddress();
    }
}
