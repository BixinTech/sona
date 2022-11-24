package cn.bixin.sona.server.room.service.redis;

import cn.bixin.sona.server.room.domain.db.MixConfig;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 木欣
 * @create 2021-04-20
 * @email baojiawei@bixin.cn
 * create by intelliJ IDEA
 */
@Component
public class StreamMixRedisRepo {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * Mix mv config redis key
     */
    private static final String MIX_MV_CONFIG = "mix:mv:config";

    /**
     * get mix mv config
     *
     * @param roomId
     * @return
     */
    private String getMixMVConfigKey(Long roomId) {
        return Joiner.on(":").join(MIX_MV_CONFIG, roomId);
    }

    /**
     * Get redis mv config
     *
     * @param roomId
     * @return
     */
    public MixConfig getMixMVConfig(Long roomId) {
        String mixConfig = redisTemplate.opsForValue().get(getMixMVConfigKey(roomId));
        return JSONObject.parseObject(mixConfig, MixConfig.class);
    }

    /**
     * Set mix mv config
     *
     * @param roomId
     * @param mixConfig
     */
    public void setMixMVConfig(Long roomId, MixConfig mixConfig) {
        //default ttl 24h
        redisTemplate.opsForValue().set(getMixMVConfigKey(roomId), JSONObject.toJSONString(mixConfig));
    }

}
