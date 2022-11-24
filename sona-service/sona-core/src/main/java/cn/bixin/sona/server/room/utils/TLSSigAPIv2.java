package cn.bixin.sona.server.room.utils;

// 使用旧版本 base64 编解码实现增强兼容性

import com.alibaba.fastjson.JSONObject;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.Deflater;

public class TLSSigAPIv2 {
    private long sdkappid;
    private String key;

    public TLSSigAPIv2(long sdkappid, String key) {
        this.sdkappid = sdkappid;
        this.key = key;
    }

    private String hmacsha256(String identifier, long currTime, long expire, String base64Userbuf) {
        String contentToBeSigned = "TLS.identifier:" + identifier + "\n"
                + "TLS.sdkappid:" + sdkappid + "\n"
                + "TLS.time:" + currTime + "\n"
                + "TLS.expire:" + expire + "\n";
        if (null != base64Userbuf) {
            contentToBeSigned += "TLS.userbuf:" + base64Userbuf + "\n";
        }
        try {
            byte[] byteKey = key.getBytes("UTF-8");
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, "HmacSHA256");
            hmac.init(keySpec);
            byte[] byteSig = hmac.doFinal(contentToBeSigned.getBytes("UTF-8"));
            return (new BASE64Encoder().encode(byteSig)).replaceAll("\\s*", "");
        } catch (UnsupportedEncodingException e) {
            return "";
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (InvalidKeyException  e) {
            return "";
        }
    }

    private String genSig(String identifier, long expire, byte[] userbuf) {
        try {
            long currTime = System.currentTimeMillis()/1000;
            JSONObject sigDoc = new JSONObject();
            sigDoc.put("TLS.ver", "2.0");
            sigDoc.put("TLS.identifier", identifier);
            sigDoc.put("TLS.sdkappid", sdkappid);
            sigDoc.put("TLS.expire", expire);
            sigDoc.put("TLS.time", currTime);

            String base64UserBuf = null;
            if (null != userbuf) {
                base64UserBuf = new BASE64Encoder().encode(userbuf);
                sigDoc.put("TLS.userbuf", base64UserBuf);
            }
            String sig = hmacsha256(identifier, currTime, expire, base64UserBuf);
            if (sig.length() == 0) {
                return "";
            }
            sigDoc.put("TLS.sig", sig);
            Deflater compressor = new Deflater();
            compressor.setInput(sigDoc.toString().getBytes(Charset.forName("UTF-8")));
            compressor.finish();
            byte [] compressedBytes = new byte[2048];
            int compressedBytesLength = compressor.deflate(compressedBytes);
            compressor.end();
            return (new String(Base64URL.base64EncodeUrl(Arrays.copyOfRange(compressedBytes,
                    0, compressedBytesLength)))).replaceAll("\\s*", "");
        } catch (Exception e) {
            return "";
        }
    }

    public String genSig(String identifier, long expire) {
        return genSig(identifier, expire, null);
    }

    public String genSigWithUserBuf(String identifier, long expire, byte[] userbuf) {
        return genSig(identifier, expire, userbuf);
    }
}
