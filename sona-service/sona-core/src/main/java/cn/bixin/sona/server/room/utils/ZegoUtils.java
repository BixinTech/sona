package cn.bixin.sona.server.room.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public class ZegoUtils {

    /**
     * 拉流端获取登录token
     *
     * @param appId   即构分配的appId
     * @param appSign 即构分配的appSign
     * @param idName  //这里的idname需要和小程序sdk前端传入的idname一致，
     *                //否则校验失败(因为这里的idname是为了校验和前端传进来的idname是否一致)。
     * @return
     */
    public static String getZeGouToken(String appId, String appSign, String idName) {
        try {
            String nonce = UUID.randomUUID().toString().replaceAll("-", "");
            long time = System.currentTimeMillis() / 1000 + 30 * 60;
            String appSign32 = appSign.replace("0x", "").replace(",", "").substring(0, 32);
            if (appSign32.length() < 32) {
                return null;
            }
            String source = md5(appId + appSign32 + idName + nonce + time);
            JSONObject json = new JSONObject();
            json.put("ver", 1);
            json.put("hash", source);
            json.put("nonce", nonce);
            json.put("expired", time);
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(json.toString().getBytes());
        } catch (Exception e) {
        }
        return null;
    }

    public static String md5(String str) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var3) {
            return null;
        }
        byte[] md5Bytes = md.digest(str.getBytes());
        return Hex.encodeHexString(md5Bytes);
    }

}
