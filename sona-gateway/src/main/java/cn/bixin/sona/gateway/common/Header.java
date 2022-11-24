package cn.bixin.sona.gateway.common;

import java.nio.charset.StandardCharsets;

/**
 * @author qinwei
 */
public class Header {

    private int type;

    private byte[] data;

    public Header() {
    }

    public Header(int type, byte[] data) {
        this.type = type;
        this.data = data;
    }

    public Header(HeaderEnum header, byte[] data) {
        this(header.getType(), data);
    }

    public Header(HeaderEnum header, String data) {
        this(header.getType(), data);
    }

    public Header(int type, String data) {
        this(type, data == null ? null : data.getBytes(StandardCharsets.UTF_8));
    }

    public int calcDataLength() {
        return data == null ? 0 : data.length;
    }

    public int calcTotalLength() {
        int dataLength = calcDataLength();
        return 1 + Varint.computeRawVarint32Size(dataLength) + dataLength;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Header{" +
                "type=" + type +
                ", data=" + (data == null ? null : new String(data, StandardCharsets.UTF_8)) +
                '}';
    }
}
