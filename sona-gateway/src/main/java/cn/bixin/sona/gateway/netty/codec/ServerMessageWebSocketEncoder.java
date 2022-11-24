package cn.bixin.sona.gateway.netty.codec;

import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.common.MessageCodec;
import cn.bixin.sona.gateway.util.AccessMessageUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.List;

/**
 * @author qinwei
 */
@ChannelHandler.Sharable
public class ServerMessageWebSocketEncoder extends MessageToMessageEncoder<AccessMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, AccessMessage msg, List<Object> out) throws Exception {
        ByteBuf buf = null;
        try {
            buf = ctx.alloc().ioBuffer();
            MessageCodec.encode(buf, msg);
            AccessMessageUtils.logOutboundMsgSize(msg);
            WebSocketFrame frame = new BinaryWebSocketFrame(buf);
            out.add(frame);
            buf = null;
        } finally {
            if (buf != null) {
                buf.release();
            }
        }
    }
}
