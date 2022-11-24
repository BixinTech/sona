package cn.bixin.sona.session.utils;

import com.google.common.base.Joiner;

/**
 * @author qinwei
 */
public class CacheKey {

    public static String getSonaChannelKey(String channelId) {
        return Joiner.on(":").join("sona:ch", channelId);
    }

    public static String getSonaUidKey(String uid) {
        return Joiner.on(":").join("sona:u", uid);
    }

    public static String getUidKey(String uid) {
        return Joiner.on(":").join("u", uid);
    }

}
