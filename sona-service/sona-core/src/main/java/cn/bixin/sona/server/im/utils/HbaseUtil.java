package cn.bixin.sona.server.im.utils;

import org.apache.hadoop.hbase.util.Bytes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HbaseUtil {

    private static final byte[] ROOM_MESSAGE_FAMILY = Bytes.toBytes("cf");

    public static String roomMessageTable(){
        return "room_message";
    }

    public static byte[] getRoomMessageFamily(){
        return Bytes.copy(ROOM_MESSAGE_FAMILY);
    }

    /**
     * 生成房间消息 rowKey
     * @param roomId
     * @param messageId
     * @return
     * @throws IOException
     */
    public static byte[] genRoomMessageKey(long roomId, String messageId) throws IOException {
        int code = String.valueOf(roomId).hashCode();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(Bytes.toBytes(code));
        output.write(Bytes.toBytes(roomId));
        output.write(Bytes.toBytes(messageId));
        return output.toByteArray();
    }

}
