package cn.bixin.sona.server.room.service.impl;

import cn.bixin.sona.common.exception.YppRunTimeException;
import cn.bixin.sona.server.exception.ExceptionCode;
import cn.bixin.sona.server.room.domain.db.ProductConfig;
import cn.bixin.sona.server.room.domain.db.RoomConfig;
import cn.bixin.sona.server.room.mapper.ProductConfigMapper;
import cn.bixin.sona.server.room.mapper.RoomConfigMapper;
import cn.bixin.sona.server.room.service.ProductConfigService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductConfigServiceImpl implements ProductConfigService {

    private static final Logger log = LoggerFactory.getLogger(ProductConfigServiceImpl.class);

    private static final Cache<String, ProductConfig> configLocalCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES).maximumSize(10000).build();
    private static final Cache<String, ProductConfig> shortCodeConfigLocalCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES).maximumSize(10000).build();
    @Resource
    private ProductConfigMapper productConfigMapper;
    @Resource
    private RoomConfigMapper roomConfigMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private String KEY_ROOM_CONFIG = "room:config";
    private String getKeyRoomConfig(long roomId) {
        return Joiner.on(":").join(KEY_ROOM_CONFIG, roomId);
    }

    @Override
    public ProductConfig getConfigInfoByCode(String productCode) {
        ProductConfig config = configLocalCache.getIfPresent(productCode);
        if (config == null) {
            config = productConfigMapper.findByProductCode(productCode);
            if (config == null) {
                throw new YppRunTimeException(ExceptionCode.PRODUCT_NOT_FOUND);
            }
            configLocalCache.put(productCode, config);
            return config;
        }

        return ProductConfig.copy(config);
    }

    @Override
    public ProductConfig getConfigInfoByShortCode(String shortCode) {
        ProductConfig config = shortCodeConfigLocalCache.getIfPresent(shortCode);
        if (config == null) {
            config = productConfigMapper.findByShortCode(shortCode);
            if (config == null) {
                throw new YppRunTimeException(ExceptionCode.PRODUCT_NOT_FOUND);
            }
            shortCodeConfigLocalCache.put(shortCode, config);
            return config;
        }

        return ProductConfig.copy(config);
    }

    @Override
    public RoomConfig getRoomConfig(long roomId) {
        String roomConfigStr = redisTemplate.opsForValue().get(getKeyRoomConfig(roomId));
        if (StringUtils.isBlank(roomConfigStr)) {
            RoomConfig roomConfig = roomConfigMapper.findRoomConfig(roomId);
            if (roomConfig == null) {
                redisTemplate.opsForValue().set(getKeyRoomConfig(roomId), "null", 1, TimeUnit.MINUTES);
                return null;
            } else {
                redisTemplate.opsForValue().set(getKeyRoomConfig(roomId), JSON.toJSONString(roomConfig), 1, TimeUnit.SECONDS);
                return roomConfig;
            }
        } else {
            if ("null".equals(roomConfigStr)) {
                return null;
            }

            return JSON.parseObject(roomConfigStr, RoomConfig.class);
        }
    }

    @Override
    public Map<Long, RoomConfig> getRoomConfigBatch(List<Long> roomIds) {
        if (CollectionUtils.isEmpty(roomIds)) {
            return Maps.newHashMap();
        }
        List<RoomConfig> list = roomConfigMapper.findRoomConfigBatch(roomIds);
        if (CollectionUtils.isEmpty(list)) {
            return Maps.newHashMap();
        }

        return list.stream().collect(Collectors.toMap(RoomConfig::getRoomId, Function.identity(), (v1, v2) -> v1));
    }

    @Override
    public boolean addRoomConfig(List<RoomConfig> roomConfigs) {
        if (CollectionUtils.isEmpty(roomConfigs)){
            return true;
        }
        for (RoomConfig roomConfig : roomConfigs) {
            log.info("addRoomConfig list, roomConfig:{}", JSONObject.toJSONString(roomConfig));

            roomConfigMapper.insertSelective(roomConfig);
            try {
                redisTemplate.opsForValue().set(getKeyRoomConfig(roomConfig.getRoomId()),
                        JSONObject.toJSONString(roomConfig), 30, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("add room config to redis error", e);
            }
        }
        return true;
    }

    @Override
    public boolean updateRoomConfigAudioStreams(List<RoomConfig> configs) {
        if (CollectionUtils.isEmpty(configs)) return false;

        roomConfigMapper.updateStreamConfigAudios(configs);
        List<Long> roomIds = configs.stream().map(RoomConfig::getRoomId).collect(Collectors.toList());
        List<String> roomConfigKeys = roomIds.stream().map(this::getKeyRoomConfig).collect(Collectors.toList());
        redisTemplate.delete(roomConfigKeys);
        return true;
    }
}
