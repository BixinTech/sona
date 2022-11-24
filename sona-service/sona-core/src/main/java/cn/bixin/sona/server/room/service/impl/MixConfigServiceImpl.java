package cn.bixin.sona.server.room.service.impl;

import cn.bixin.sona.server.room.domain.convert.MixConfigConverter;
import cn.bixin.sona.server.room.domain.db.MixConfig;
import cn.bixin.sona.server.room.mapper.MixConfigMapper;
import cn.bixin.sona.server.room.service.MixConfigService;
import cn.bixin.sona.server.room.service.redis.StreamMixRedisRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author 木欣
 * @create 2021-05-27
 * @email baojiawei@bixin.cn
 * create by intelliJ IDEA
 */
@Component
public class MixConfigServiceImpl implements MixConfigService {

    private static final Logger log = LoggerFactory.getLogger(MixConfigServiceImpl.class);

    @Autowired
    private MixConfigMapper mixConfigMapper;
    @Autowired
    private StreamMixRedisRepo streamMixRedisRepo;

    @Override
    public void createMixConfig(MixConfig mixConfig) {
        int count = mixConfigMapper.insertOrUpdate(mixConfig);
        if (count > 0) {
            streamMixRedisRepo.setMixMVConfig(mixConfig.getRoomId(), mixConfig);
        }
    }

    @Override
    public void updateMixConfig(MixConfig mixConfig) {
        int count = mixConfigMapper.updateMixConfig(mixConfig);
        if (count > 0) {
            streamMixRedisRepo.setMixMVConfig(mixConfig.getRoomId(), mixConfig);
        }
    }

    @Override
    public MixConfig getMixConfigByRoomId(Long roomId) {
        MixConfig config = streamMixRedisRepo.getMixMVConfig(roomId);
        if (Objects.isNull(config)) {
            return mixConfigMapper.getMixConfigByRoomId(roomId);
        }
        return config;
    }

    @Override
    public MixConfig getMixConfigOrDefaultAudioByRoomId(Long roomId) {
        MixConfig mixConfig = getMixConfigByRoomId(roomId);
        if (Objects.isNull(mixConfig)) {
            log.info("mix config is null, get default config, roomId: {}", roomId);
            mixConfig = MixConfigConverter.defaultAudioMixConfig(roomId, "");
        }
        return mixConfig;
    }

}
