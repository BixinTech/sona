package cn.bixin.sona.gateway.common;

import io.netty.buffer.ByteBuf;

/**
 * @author qinwei
 */
public final class Bytes {

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    private Bytes() {
    }

    public static byte[] int2bytes(int v) {
        byte[] ret = {0, 0, 0, 0};
        int2bytes(v, ret);
        return ret;
    }

    public static void int2bytes(int v, byte[] b) {
        int2bytes(v, b, 0);
    }

    public static void int2bytes(int v, byte[] b, int off) {
        b[off + 3] = (byte) v;
        b[off + 2] = (byte) (v >>> 8);
        b[off + 1] = (byte) (v >>> 16);
        b[off + 0] = (byte) (v >>> 24);
    }


    public static int bytes2int(byte[] b) {
        return bytes2int(b, 0);
    }

    public static int bytes2int(byte[] b, int off) {
        return ((b[off + 3] & 0xFF) << 0) +
                ((b[off + 2] & 0xFF) << 8) +
                ((b[off + 1] & 0xFF) << 16) +
                ((b[off + 0]) << 24);
    }

    public static byte[] getFromBuf(ByteBuf in, int length) {
        if (length <= 0) {
            return EMPTY_BYTE_ARRAY;
        }
        byte[] bTemp = new byte[length];
        in.readBytes(bTemp);
        return bTemp;
    }

}
