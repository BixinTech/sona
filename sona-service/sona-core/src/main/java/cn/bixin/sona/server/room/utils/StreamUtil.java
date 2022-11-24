package cn.bixin.sona.server.room.utils;
import cn.bixin.sona.enums.RoomMixedEnum;
import cn.bixin.sona.server.room.domain.db.ProductConfig;
import cn.bixin.sona.server.room.domain.db.RoomConfig;

import java.util.Objects;

import static cn.bixin.sona.enums.RoomMixedEnum.MIXED_TECENT_MIXED;
import static cn.bixin.sona.enums.RoomMixedEnum.MIXED_ZEGO_MIXED;


public class StreamUtil {

    public static boolean isRoomStreamEqual(RoomConfig productConfig, RoomMixedEnum mix) {
        return mix.getPullMode().equals(productConfig.getPullMode()) &&
                mix.getPushMode().equals(mix.getPushMode()) && mix.getSupplier().equals(productConfig.getStreamSupplier());
    }

    public static boolean isProductStreamEqual(ProductConfig productConfig, RoomMixedEnum mix) {
        return mix.getPullMode().equals(productConfig.getPullMode()) &&
                mix.getPushMode().equals(mix.getPushMode()) && mix.getSupplier().equals(productConfig.getStreamSupplier());
    }


    /**
     * 若是腾讯异常，则切换ZEGO混流
     * 若是即构异常，则切换腾讯混流
     *
     * @param errorCloudProvider
     * @param event
     * @return
     */
    public static RoomMixedEnum getStreamScheme(String errorCloudProvider,
                                                String event) {
        if ("TENCENT".equalsIgnoreCase(errorCloudProvider)) {
            return MIXED_ZEGO_MIXED;
        } else {
            return MIXED_TECENT_MIXED;
        }
    }
}
