package cn.bixin.sona.gateway.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

/**
 * @author qinwei
 */
public final class DeflaterCompress {

    private DeflaterCompress() {
    }

    public static byte[] compress(byte[] input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(input.length);
        DeflaterOutputStream dos = new DeflaterOutputStream(baos);
        dos.write(input);
        dos.close();
        return baos.toByteArray();
    }

    public static byte[] decompress(byte[] input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(input.length);
        InflaterOutputStream ios = new InflaterOutputStream(baos);
        ios.write(input);
        ios.close();
        return baos.toByteArray();
    }

}
