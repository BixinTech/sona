package cn.bixin.sona.gateway.netty.codec;

import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.common.MessageCodec;
import cn.bixin.sona.gateway.exception.AccessMessageDecodeException;
import cn.bixin.sona.gateway.util.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.List;

/**
 * @author qinwei
 */
@ChannelHandler.Sharable
public class ServerMessageWebSocketDecoder extends MessageToMessageDecoder<WebSocketFrame> {

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        if (msg instanceof BinaryWebSocketFrame) {
            ByteBuf buf = msg.content();
            if (buf == null) {
                return;
            }
            int flag = buf.getByte(buf.readerIndex());
            if (flag != 0 && flag != 1) {
                throw new AccessMessageDecodeException("unknown flag, flag=" + buf.getByte(buf.readerIndex()));
            }
            if (buf.readableBytes() < Constants.PROTOCOL_META_LEN) {
                throw new AccessMessageDecodeException("current ByteBuf length less than meta, len=" + buf.readableBytes());
            }
            AccessMessage message = MessageCodec.decode(buf);
            out.add(message);
        } else {
            throw new AccessMessageDecodeException("unsupported frame type: " + msg.getClass().getName());
        }
    }

}
