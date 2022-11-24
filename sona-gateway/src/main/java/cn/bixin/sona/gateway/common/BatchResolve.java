package cn.bixin.sona.gateway.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 不能用 Lambda 表达式，安卓是复用这个代码，使用 Lambda 的话可能会报错
 *
 * @author qinwei
 */
public final class BatchResolve {

    private BatchResolve() {
    }

    public static List<AccessMessage> resolve(AccessMessage message) {
        List<Header> headers = message.getHeaders();
        if (headers == null || headers.isEmpty()) {
            return Collections.singletonList(message);
        }
        int size = getBatchSize(headers);
        if (size == 0) {
            return Collections.singletonList(message);
        }
        ByteBuf byteBuf = Unpooled.wrappedBuffer(message.getBody());
        List<AccessMessage> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            AccessMessage msg = new AccessMessage(message);
            msg.setBody(Bytes.getFromBuf(byteBuf, byteBuf.readInt()));
            list.add(msg);
        }
        return list;
    }

    private static int getBatchSize(List<Header> headers) {
        for (Header header : headers) {
            if (HeaderEnum.BATCH.getType() == header.getType()) {
                return Integer.parseInt(new String(header.getData(), StandardCharsets.UTF_8));
            }
        }
        return 0;
    }

    public static AccessMessage merge(List<AccessMessage> list) {
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.heapBuffer();
        for (AccessMessage message : list) {
            byte[] body = message.getBody();
            byteBuf.writeInt(body.length);
            byteBuf.writeBytes(body);
        }
        AccessMessage message = new AccessMessage(list.get(0));
        message.addHeader(new Header(HeaderEnum.BATCH, String.valueOf(list.size())));
        message.setBody(Bytes.getFromBuf(byteBuf, byteBuf.readableBytes()));
        byteBuf.release();
        return message;
    }

}
