package cn.bixin.sona.gateway.channel;

import cn.bixin.sona.gateway.cat.MonitorUtils;
import cn.bixin.sona.gateway.channel.support.ChannelAttrs;
import cn.bixin.sona.gateway.channel.support.NettyFuture;
import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.concurrent.buffer.ChannelBuffer;
import cn.bixin.sona.gateway.exception.RemoteException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.internal.PlatformDependent;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author qinwei
 * <p>
 * 对 netty 底层 channel 的封装，业务层直接操作 NettyChannel，无需关心底层channel的处理
 */
@Slf4j
public class NettyChannel extends AbstractChannel {

    private static final ConcurrentMap<String, NettyChannel> CHANNEL_MAP = PlatformDependent.newConcurrentHashMap(1 << 14);

    private static final ConcurrentMap<Integer, AtomicInteger> CHANNEL_TYPE_COUNT_MAP = PlatformDependent.newConcurrentHashMap();

    private static final AtomicInteger UN_AUTH_COUNT = new AtomicInteger();

    /**
     * 减少内存使用
     */
    private static final AtomicIntegerFieldUpdater<NettyChannel> FLUSH_UPDATER = AtomicIntegerFieldUpdater.newUpdater(NettyChannel.class, "flush");

    private static final AtomicIntegerFieldUpdater<NettyChannel> SEQUECE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(NettyChannel.class, "sequece");

    private final ConcurrentMap<String, Object> attributes = PlatformDependent.newConcurrentHashMap();

    private final Channel channel;

    private final AtomicBoolean auth = new AtomicBoolean(false);

    private final ChannelBuffer buffer = new ChannelBuffer();

    private volatile int flush;

    private volatile int sequece;

    private NettyChannel(Channel channel) {
        this.channel = channel;
    }

    public static NettyChannel getOrAddChannel(Channel channel) {
        if (channel == null) {
            return null;
        }
        String channelId = ChannelAttrs.getChannelId(channel);
        NettyChannel result = CHANNEL_MAP.get(channelId);
        if (result == null) {
            NettyChannel nettyChannel = new NettyChannel(channel);
            UN_AUTH_COUNT.incrementAndGet();
            if (channel.isActive()) {
                nettyChannel.markActive(true);
                result = CHANNEL_MAP.putIfAbsent(channelId, nettyChannel);
            }
            if (result == null) {
                result = nettyChannel;
            }
        }
        return result;
    }

    public static void removeChannel(Channel channel) {
        if (channel == null) {
            return;
        }
        NettyChannel nettyChannel = CHANNEL_MAP.remove(ChannelAttrs.getChannelId(channel));
        if (nettyChannel != null) {
            nettyChannel.markActive(false);
            if (nettyChannel.isAuth()) {
                int channelType = nettyChannel.getAttrs().getChannelType();
                CHANNEL_TYPE_COUNT_MAP.computeIfAbsent(channelType, k -> new AtomicInteger()).decrementAndGet();
            } else {
                UN_AUTH_COUNT.decrementAndGet();
            }
        }
    }

    public static void addChannelTypeCount(NettyChannel channel) {
        if (channel.isConnected()) {
            int channelType = channel.getAttrs().getChannelType();
            CHANNEL_TYPE_COUNT_MAP.computeIfAbsent(channelType, k -> new AtomicInteger()).incrementAndGet();
        }
    }

    public static Map<Integer, AtomicInteger> channelTypeCount() {
        return CHANNEL_TYPE_COUNT_MAP;
    }

    public static int authChannelCount() {
        return Math.max(CHANNEL_MAP.size() - unAuthChannelCount(), 0);
    }

    public static int unAuthChannelCount() {
        return UN_AUTH_COUNT.get();
    }

    public static NettyChannel getChannel(String channelId) {
        return CHANNEL_MAP.get(channelId);
    }

    public static Collection<NettyChannel> getAllChannels() {
        return CHANNEL_MAP.values();
    }

    public boolean markAuth() {
        if (isConnected() && auth.compareAndSet(false, true)) {
            UN_AUTH_COUNT.decrementAndGet();
            return true;
        }
        return false;
    }

    public int getSequece() {
        return SEQUECE_UPDATER.incrementAndGet(this);
    }

    public boolean isAuth() {
        return auth.get();
    }

    public String getChannelId() {
        return getAttrs().getChannelId();
    }

    public ChannelAttrs getAttrs() {
        return ChannelAttrs.getAttrs(channel);
    }

    public ChannelAttrs getAttrsIfExists() {
        return ChannelAttrs.getAttrsIfExists(channel);
    }

    public boolean isWritable() {
        return channel.isWritable();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) channel.localAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }

    public String getUid() {
        return getAttrs().getUid();
    }

    public void fastSend(AccessMessage message, boolean immediate) {
        if (immediate) {
            fastSendNow(message);
        } else {
            fastSendLater(message);
        }
    }

    private void fastSendNow(AccessMessage msg) {
        if (canSend(msg)) {
            channel.writeAndFlush(msg).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        }
    }

    private void fastSendLater(AccessMessage msg) {
        buffer.offer(msg, this::fastSendNow);
        if (FLUSH_UPDATER.compareAndSet(this, 0, 1)) {
            channel.eventLoop().schedule(this::flush, 50, TimeUnit.MILLISECONDS);
        }
    }

    private void flush() {
        while (FLUSH_UPDATER.getAndSet(this, 0) == 1) {
            buffer.drainTo(this::fastSendNow);
        }
    }

    private boolean canSend(AccessMessage message) {
        if (!isConnected()) {
            MonitorUtils.logCatEventWithMessage(MonitorUtils.SEND_MESSAGE_PROBLEM, "ChannelNotActive", message, false);
            return false;
        }
        if (!isWritable()) {
            MonitorUtils.logCatEventWithMessage(MonitorUtils.SEND_MESSAGE_PROBLEM, "ChannelNotWritable", message, false);
            return false;
        }
        return true;
    }

    @Override
    public NettyFuture request(AccessMessage request, int timeout) throws RemoteException {
        NettyFuture future = NettyFuture.newFuture(request.getId(), timeout, this);
        try {
            send(request, true, false);
        } catch (RemoteException e) {
            future.cancel(e);
        }
        return future;
    }

    @Override
    public void send(Object message, boolean sent, boolean closeWhenComplete) throws RemoteException {
        super.send(message, sent, closeWhenComplete);

        String name;
        if (!(message instanceof AccessMessage)) {
            name = message.getClass().getSimpleName();
        } else {
            AccessMessage msg = (AccessMessage) message;
            name = msg.isHeartbeat() ? "HB" : String.valueOf(msg.getCmd());
        }
        MonitorUtils.newTransaction(MonitorUtils.CAT_OUT_TRANS_TYPE, name, () -> doSend(message, sent, closeWhenComplete));
    }

    private void doSend(Object message, boolean sent, boolean closeWhenComplete) throws RemoteException {
        boolean success = true;
        int timeout = 1000;
        try {
            ChannelFuture future = channel.writeAndFlush(message);
            if (closeWhenComplete) {
                future.addListener((ChannelFutureListener) f -> close());
            }
            if (sent) {
                success = future.await(timeout);
            }
            Throwable cause = future.cause();
            if (cause != null) {
                throw cause;
            } else {
                future.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            }
        } catch (Throwable t) {
            throw new RemoteException(this, "Failed to send message " + message + " to " + getRemoteAddress() + ", cause: " + t.getMessage(), t);
        }
        if (!success) {
            throw new RemoteException(this, "Failed to send message " + message + " to " + getRemoteAddress() + "in timeout(" + timeout + "ms) limit");
        }
    }

    @Override
    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    @Override
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public <T> T getAttribute(String key, Class<T> type) {
        return type.cast(getAttribute(key));
    }

    public <T> T removeAttribute(String key, Class<T> type) {
        return type.cast(removeAttribute(key));
    }

    @Override
    public void setAttribute(String key, Object value) {
        if (value != null) {
            attributes.put(key, value);
        } else {
            removeAttribute(key);
        }
    }

    @Override
    public Object removeAttribute(String key) {
        return attributes.remove(key);
    }

    public void clear() {
        attributes.clear();
    }

    @Override
    public void close() {
        try {
            super.close();
            channel.close();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    @Override
    public final int hashCode() {
        return channel.id().hashCode();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NettyChannel)) {
            return false;
        }
        return ((NettyChannel) o).channel.id().asLongText().equals(this.channel.id().asLongText());
    }

    @Override
    public String toString() {
        return "NettyChannel [channelId=" + ChannelAttrs.getChannelId(channel) + ", channel=" + channel + "]";
    }
}
