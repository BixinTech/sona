package cn.bixin.sona.console.convert;

import cn.bixin.sona.console.domain.db.ProductConfig;
import cn.bixin.sona.console.domain.db.RoomConfig;
import cn.bixin.sona.console.domain.dto.MediaRoomInfoDTO;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;

public class BeanCreator {
    private static final String STEAM_URL_PREFIX = "rtmp://rtmp-ws.bxapp.cn/bxapp-ws/";

    public static MediaRoomInfoDTO createMediaRoomInfoDTO(RoomConfig rc) {
        if (Objects.isNull(rc)) {
            return null;
        }
        MediaRoomInfoDTO mediaRoomInfoDTO = new MediaRoomInfoDTO();
        mediaRoomInfoDTO.setRoomId(rc.getRoomId() + "");
        mediaRoomInfoDTO.setCloudType(rc.getStreamSupplier());
        mediaRoomInfoDTO.setPullStreamType(rc.getPullMode());
        mediaRoomInfoDTO.setStreamUrl(STEAM_URL_PREFIX + rc.getRoomId());
        return mediaRoomInfoDTO;
    }

    public static MediaRoomInfoDTO createMediaRoomInfoDTO(Long roomId, Map<String, ProductConfig> productConfigMap, Map<Long, String> roomProductMap) {
        if (CollectionUtils.isEmpty(productConfigMap) || CollectionUtils.isEmpty(roomProductMap)) {
            return null;
        }
        String productCode = roomProductMap.get(roomId);
        ProductConfig pc = productConfigMap.get(productCode);
        MediaRoomInfoDTO mediaRoomInfoDTO = new MediaRoomInfoDTO();
        mediaRoomInfoDTO.setRoomId(roomId + "");
        mediaRoomInfoDTO.setCloudType(pc.getStreamSupplier());
        mediaRoomInfoDTO.setPullStreamType(pc.getPullMode());
        mediaRoomInfoDTO.setStreamUrl(STEAM_URL_PREFIX + roomId);
        return mediaRoomInfoDTO;
    }
}
