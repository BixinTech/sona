package cn.bixin.sona.server.room.domain.convert;

import cn.bixin.sona.dto.StreamConfigInfoDTO;
import cn.bixin.sona.server.room.domain.db.ProductConfig;
import cn.bixin.sona.server.room.domain.stream.UserSginInputParam;

public class StreamConfigInfoConverter {

    public static StreamConfigInfoDTO convertStreamConfig(ProductConfig config) {
        StreamConfigInfoDTO streamConfig = new StreamConfigInfoDTO();
        streamConfig.setPullMode(config.getPullMode());
        streamConfig.setPushMode(config.getPushMode());
        streamConfig.setSupplier(config.getStreamSupplier());
        streamConfig.setType(config.getType());
        streamConfig.setBitrate(config.getBitrate());

        return streamConfig;
    }

    public static UserSginInputParam convertUserSignInputParam(ProductConfig productConfig, long uid, boolean isGuest) {
        if (productConfig == null) return null;

        UserSginInputParam userSginInputParam = new UserSginInputParam();
        userSginInputParam.setStreamSupplier(productConfig.getStreamSupplier());
        userSginInputParam.setNeedReplay(productConfig.isNeedReplay());
        userSginInputParam.setGuest(isGuest);
        userSginInputParam.setUid(uid);
        userSginInputParam.setProductCode(productConfig.getProductCode());
        return userSginInputParam;
    }
}
