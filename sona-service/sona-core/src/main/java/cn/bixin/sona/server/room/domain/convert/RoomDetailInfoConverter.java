package cn.bixin.sona.server.room.domain.convert;

import cn.bixin.sona.dto.ImConfigInfoDTO;
import cn.bixin.sona.dto.ProductConfigInfoDTO;
import cn.bixin.sona.dto.RoomDTO;
import cn.bixin.sona.dto.RoomDetailInfoDTO;
import cn.bixin.sona.dto.StreamConfigInfoDTO;
import cn.bixin.sona.server.room.domain.db.ProductConfig;
import cn.bixin.sona.server.room.domain.enums.ProductEnum;
import com.ctrip.framework.apollo.ConfigService;

public class RoomDetailInfoConverter {

    public static RoomDetailInfoDTO convertDetailObj(ProductConfig config,
                                                     RoomDTO room,
                                                     StreamConfigInfoDTO streamConfigInfo) {
        RoomDetailInfoDTO roomDetailInfoDTO = new RoomDetailInfoDTO();
        ProductConfigInfoDTO productConfigInfoDTO = new ProductConfigInfoDTO();
        fillProductConfigInfo(productConfigInfoDTO, config, streamConfigInfo);

        roomDetailInfoDTO.setProductConfig(productConfigInfoDTO);
        roomDetailInfoDTO.setRoomId(room.getRoomId());
        roomDetailInfoDTO.setOwnerUid(room.getUid());

        return roomDetailInfoDTO;
    }

    public static RoomDetailInfoDTO convertDetailObjGuest(ProductConfig config, RoomDTO room, StreamConfigInfoDTO streamConfigInfo, long guestUid) {
        RoomDetailInfoDTO roomDetailInfoDTO = convertDetailObj(config, room, streamConfigInfo);
        roomDetailInfoDTO.setGuestUid(guestUid);
        roomDetailInfoDTO.setNickname("游客" + guestUid % 1000000);

        return roomDetailInfoDTO;
    }


    private static void fillProductConfigInfo(ProductConfigInfoDTO productConfigInfoDTO,
                                              ProductConfig config, StreamConfigInfoDTO streamConfigInfo) {
        ImConfigInfoDTO imConfigInfoDTO = new ImConfigInfoDTO();
        if (config != null) {
            imConfigInfoDTO.setModule(config.getImModule());
            imConfigInfoDTO.setImSendType(config.getImSendType());
            productConfigInfoDTO.setProductCode(config.getProductCode());
            ProductEnum productEnum = ProductEnum.getPlatformByName(config.getProductCode());
            productConfigInfoDTO.setProductCodeAlias(productEnum != null ? productEnum.getCode() : "");
        }

        imConfigInfoDTO.setArrivalMessageSwitch(true);
        imConfigInfoDTO.setMessageExpireTime(ConfigService.getAppConfig().getIntProperty("messageExpireTime", 40000));
        imConfigInfoDTO.setClientQueueSize(ConfigService.getAppConfig().getIntProperty("clientQueueSize", 600));
        productConfigInfoDTO.setImConfig(imConfigInfoDTO);
        productConfigInfoDTO.setStreamConfig(streamConfigInfo);
    }
}
