package cn.bixin.sona.gateway.netty;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.SystemPropertyUtil;
import net.openhft.affinity.AffinityStrategies;
import net.openhft.affinity.AffinityThreadFactory;

import java.util.concurrent.ThreadFactory;

/**
 * @author qinwei
 */
public class NettyFactory {

    public static EventLoopGroup eventLoopGroup(int threads, String threadName) {
        ThreadFactory threadFactory = new AffinityThreadFactory(threadName, AffinityStrategies.DIFFERENT_CORE);
        return supportEpoll() ? new EpollEventLoopGroup(threads, threadFactory) : new NioEventLoopGroup(threads, threadFactory);
    }

    public static Class<? extends SocketChannel> socketChannelClass() {
        return supportEpoll() ? EpollSocketChannel.class : NioSocketChannel.class;
    }

    public static Class<? extends ServerSocketChannel> serverSocketChannelClass() {
        return supportEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }

    private static boolean supportEpoll() {
        return SystemPropertyUtil.get("os.name").toLowerCase().contains("linux") && Epoll.isAvailable();
    }
}
