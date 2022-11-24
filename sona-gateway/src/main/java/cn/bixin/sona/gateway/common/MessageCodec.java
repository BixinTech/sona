package cn.bixin.sona.gateway.common;

import cn.bixin.sona.gateway.exception.AccessMessageDecodeException;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qinwei
 */
public final class MessageCodec {

    private static final int MIN_BODY_SIZE_TO_COMPRESS = 2048;

    private MessageCodec() {
    }

    public static void encode(ByteBuf buf, AccessMessage msg) throws Exception {
        buf.writeBoolean(msg.isReq());
        buf.writeBoolean(msg.isTwoWay());
        buf.writeBoolean(msg.isHeartbeat());
        buf.writeByte(msg.getVersion());
        Varint.writeRawVarint32(buf, msg.getId());
        if (msg.isHeartbeat()) {
            return;
        }
        buf.writeByte(msg.getCmd());

        compress(msg);

        List<Header> headers = msg.getHeaders();
        int headerCount = headers == null ? 0 : headers.size();
        int headerLength = 0;
        for (int i = 0; i < headerCount; i++) {
            headerLength += headers.get(i).calcTotalLength();
        }

        byte[] body = msg.getBody();
        int bodyLength = body == null ? 0 : body.length;
        msg.setLength(headerLength + bodyLength);
        Varint.writeRawVarint32(buf, msg.getLength());

        buf.writeByte(headerCount);

        for (int i = 0; i < headerCount; i++) {
            Header header = headers.get(i);
            buf.writeByte(header.getType());
            Varint.writeRawVarint32(buf, header.calcDataLength());
            buf.writeBytes(header.getData());
        }

        if (bodyLength != 0) {
            buf.writeBytes(body);
        }
    }

    private static void compress(AccessMessage msg) throws IOException {
        byte[] body = msg.getBody();
        if (body != null && body.length >= MIN_BODY_SIZE_TO_COMPRESS) {
            msg.setBody(DeflaterCompress.compress(body));
            msg.addHeader(new Header(HeaderEnum.COMPRESS, "1"));
        }
    }

    public static AccessMessage decode(ByteBuf buf) {
        AccessMessage msg = new AccessMessage();
        try {
            msg.setReq(buf.readBoolean());
            msg.setTwoWay(buf.readBoolean());
            msg.setHeartbeat(buf.readBoolean());
            msg.setVersion(buf.readByte());
            msg.setId(Varint.readRawVarint32(buf));
            if (msg.isHeartbeat()) {
                return msg;
            }
            msg.setCmd(buf.readByte());
            msg.setLength(Varint.readRawVarint32(buf));

            int bodyLength = msg.getLength();
            boolean compressed = false;
            List<Header> headers = new ArrayList<>();
            int headerCount = buf.readByte();
            for (int i = 0; i < headerCount; i++) {
                int headerType = buf.readByte();
                int headerLength = Varint.readRawVarint32(buf);
                if (buf.readableBytes() < headerLength) {
                    throw new AccessMessageDecodeException("given header len is wrong, curHeaderType=" + headerType + ", headerLength=" + headerLength + ", current ByteBuf length=" + buf.readableBytes());
                }
                Header header = new Header(headerType, Bytes.getFromBuf(buf, headerLength));
                headers.add(header);
                if (headerType == HeaderEnum.COMPRESS.getType()) {
                    compressed = true;
                }
                bodyLength -= header.calcTotalLength();
            }
            msg.setHeaders(headers);

            byte[] body = Bytes.getFromBuf(buf, bodyLength);
            if (compressed) {
                body = DeflaterCompress.decompress(body);
            }
            msg.setBody(body);
            return msg;
        } catch (Exception e) {
            if (e instanceof AccessMessageDecodeException) {
                throw (AccessMessageDecodeException) e;
            }
            throw new AccessMessageDecodeException("msg decoder failed!", e);
        }
    }

}
