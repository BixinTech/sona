package cn.bixin.sona.gateway.netty;

import cn.bixin.sona.gateway.channel.handler.ChannelHandlerWrap;
import cn.bixin.sona.gateway.channel.handler.MercuryServerHandler;
import cn.bixin.sona.gateway.netty.codec.ServerMessageDecoder;
import cn.bixin.sona.gateway.netty.codec.ServerMessageEncoder;
import cn.bixin.sona.gateway.netty.codec.ServerMessageWebSocketDecoder;
import cn.bixin.sona.gateway.netty.codec.ServerMessageWebSocketEncoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;


/**
 * @author qinwei
 */
@Slf4j
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final NettyServerHandler NETTY_SERVER_HANDLER = new NettyServerHandler(ChannelHandlerWrap.wrap(new MercuryServerHandler()));

    private static final ServerMessageEncoder TCP_ENCODER = new ServerMessageEncoder();

    private static final ServerMessageWebSocketEncoder WEBSOCKET_ENCODER = new ServerMessageWebSocketEncoder();

    private static final ServerMessageWebSocketDecoder WEBSOCKET_DECODER = new ServerMessageWebSocketDecoder();

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        int serverPort = socketChannel.localAddress().getPort();
        if (serverPort == NettyServer.PORT) {
            pipeline.addLast("encoder", TCP_ENCODER);
            pipeline.addLast("decoder", new ServerMessageDecoder());
        } else if (serverPort == NettyServer.PORT_WS) {
            pipeline.addLast("httpServerCodec", new HttpServerCodec());
            pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(2048));
            pipeline.addLast("webSocketServerProtocolHandler", new WebSocketServerProtocolHandler("/ws"));
            pipeline.addLast("encoder", WEBSOCKET_ENCODER);
            pipeline.addLast("decoder", WEBSOCKET_DECODER);
        }
        pipeline.addLast("handler", NETTY_SERVER_HANDLER);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.error("exceptionCaught, remoteAddress=" + ctx.channel().remoteAddress(), cause);
    }

}
