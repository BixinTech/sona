package cn.bixin.sona.server.room.service;

import cn.bixin.sona.server.room.domain.db.ProductConfig;
import cn.bixin.sona.server.room.domain.db.RoomConfig;

import java.util.List;
import java.util.Map;

public interface ProductConfigService {

    /**
     * 查询配置信息
     *
     * @param productCode: 产品code
     * @return: config info
     */
    ProductConfig getConfigInfoByCode(String productCode);

    ProductConfig getConfigInfoByShortCode(String shortCode);

    /**
     * 获取房间配置信息
     * @param roomId: roomId
     * @return config info
     */
    RoomConfig getRoomConfig(long roomId);

    Map<Long, RoomConfig> getRoomConfigBatch(List<Long> roomIds);

    boolean addRoomConfig(List<RoomConfig> wrapNewRoomConfig);

    boolean updateRoomConfigAudioStreams(List<RoomConfig> configs);
}
