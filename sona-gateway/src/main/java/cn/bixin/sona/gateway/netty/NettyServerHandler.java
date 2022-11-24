package cn.bixin.sona.gateway.netty;

import cn.bixin.sona.gateway.channel.support.ChannelAttrs;
import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.channel.handler.ChannelHandler;
import cn.bixin.sona.gateway.util.NetUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author qinwei
 */
@Slf4j
@io.netty.channel.ChannelHandler.Sharable
public class NettyServerHandler extends ChannelDuplexHandler {

    private final ChannelHandler handler;

    public NettyServerHandler(ChannelHandler handler) {
        this.handler = handler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int serverPort = NetUtil.getPort(ctx.channel().localAddress());
        if (serverPort == NettyServer.PORT) {
            // 非websocket连接初始化
            initChannel(ctx.channel(), null);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel());
        try {
            log.info("The connection of {} -> {} is disconnected, channelId={}", channel.getRemoteAddress(), channel.getLocalAddress(), channel.getChannelId());
            handler.disconnect(channel);
        } finally {
            NettyChannel.removeChannel(ctx.channel());
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel());
        handler.receive(channel, msg);
        //对于 websocket 场景, 这里接收到的 msg 可能是 websocketframe， 它是属于 ReferenceCounted,
        // 需要触发 TailContext 的 channelRead 去执行 ReferenceCountUtil.release(msg)
        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel());
        handler.send(channel, msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            int serverPort = NetUtil.getPort(ctx.channel().localAddress());
            if (serverPort == NettyServer.PORT_WS) {
                // websocket连接初始化
                InetSocketAddress remoteAddr = NetUtil.getWsRemoteAddrFromHeader(handshakeComplete.requestHeaders(), ctx.channel());
                initChannel(ctx.channel(), remoteAddr);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel());
        handler.caught(channel, cause);
    }

    private NettyChannel initChannel(Channel ch, InetSocketAddress remoteAddr) throws Exception {
        if (remoteAddr == null) {
            remoteAddr = (InetSocketAddress) ch.remoteAddress();
        }
        ChannelAttrs.init(ch, remoteAddr);
        NettyChannel channel = NettyChannel.getOrAddChannel(ch);
        log.info("The connection of {} -> {} is established, channelId={}", channel.getRemoteAddress(), channel.getLocalAddress(), channel.getChannelId());
        handler.connect(channel);
        return channel;
    }
}
