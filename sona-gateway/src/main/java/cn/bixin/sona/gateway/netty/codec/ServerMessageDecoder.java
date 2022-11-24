package cn.bixin.sona.gateway.netty.codec;

import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.common.MessageCodec;
import cn.bixin.sona.gateway.exception.AccessMessageDecodeException;
import cn.bixin.sona.gateway.util.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author qinwei
 */
public class ServerMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int flag = in.getByte(in.readerIndex());
        if (flag != 0 && flag != 1) {
            throw new AccessMessageDecodeException("unknown flag, flag=" + in.getByte(in.readerIndex()));
        }
        if (in.readableBytes() < Constants.PROTOCOL_META_LEN) {
            throw new AccessMessageDecodeException("current ByteBuf length less than meta, len=" + in.readableBytes());
        }
        AccessMessage message = MessageCodec.decode(in);
        out.add(message);
    }

}
