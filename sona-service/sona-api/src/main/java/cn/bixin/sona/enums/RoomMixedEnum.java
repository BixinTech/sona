package cn.bixin.sona.enums;

import cn.bixin.sona.common.dto.Code;
import cn.bixin.sona.common.exception.YppRunTimeException;

import java.util.Arrays;

public enum RoomMixedEnum {
    /**
     * 聊天室混流方案枚举
     *
     * code desc supplier pull push
     */
    MIXED_ZEGO_MIXED(0, "即构混流", "ZEGO", "MIXED", "MULTI"){
        @Override
        public RoomMixedEnum getSwitchTarget() {
            return MIXED_TECENT_MIXED;
        }
    },
    MIXED_ZEGO_SINGLE(1, "即构单流", "ZEGO","MULTI", "MULTI"){
        @Override
        public RoomMixedEnum getSwitchTarget() {
            return MIXED_ZEGO_MIXED;
        }
    },
    MIXED_AGORAL_SINGLE(2, "声网", "WANGSU","MULTI", "MULTI"),
    MIXED_ZEGO_PUSH_ON_OPEN(3, "即构开麦推流", "ZEGO","MULTI", "MULTI"),
    MIXED_TECENT_CLOUD(4, "腾讯单流", "TENCENT","MULTI", "MULTI"){
        @Override
        public RoomMixedEnum getSwitchTarget() {
            return MIXED_TECENT_MIXED;
        }
    },
    MIXED_TECENT_MIXED(5, "腾讯混流", "TENCENT","MIXED", "MULTI"){
        @Override
        public RoomMixedEnum getSwitchTarget() {
            return MIXED_ZEGO_MIXED;
        }
    },
    ;

    private int code;
    private String desc;
    private String supplier;
    private String pullMode;
    private String pushMode;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getPullMode() {
        return pullMode;
    }

    public String getPushMode() {
        return pushMode;
    }

    public static String getSupplier(int code) {
        for (RoomMixedEnum each : RoomMixedEnum.values()) {
            if (each.getCode() == code) {
                return each.getSupplier();
            }
        }

        return "";
    }

    public static RoomMixedEnum getRoomMixedEnum(int code) {
        return Arrays.stream(RoomMixedEnum.values()).
                filter(rme -> rme.code == code)
                .findFirst().
                orElseThrow(() -> new YppRunTimeException(
                        Code.business("8010", "不存在的枚举类型"))
                );
    }

    public static String getPullMode(int code) {
        for (RoomMixedEnum each : RoomMixedEnum.values()) {
            if (each.getCode() == code) {
                return each.getPullMode();
            }
        }

        return "";
    }

    public static String getPushMode(int code) {
        for (RoomMixedEnum each : RoomMixedEnum.values()) {
            if (each.getCode() == code) {
                return each.getPushMode();
            }
        }

        return "";
    }

    public static RoomMixedEnum getBySupplierAndMode(String supplier, String mode) {
        for (RoomMixedEnum each : RoomMixedEnum.values()) {
            if (each.getSupplier().equals(supplier) && each.getPullMode().equals(mode)) {
                return each;
            }
        }

        return null;
    }

    public RoomMixedEnum getSwitchTarget(){
        return null;
    }


    public static String convertMixed(int code) {
        if (code == MIXED_ZEGO_MIXED.code) {
            return "0";
        }
        if (code == MIXED_ZEGO_SINGLE.code) {
            return "1";
        }
        if (code == MIXED_AGORAL_SINGLE.code) {
            return "2";
        }
        if (code == MIXED_ZEGO_PUSH_ON_OPEN.code) {
            return "3";
        }
        if (code == MIXED_TECENT_CLOUD.code) {
            return "4";
        }
        if (code == MIXED_TECENT_MIXED.code) {
            return "5";
        }

        return "1";
    }

    RoomMixedEnum(int code, String desc, String supplier, String pullMode, String pushMode) {
        this.code = code;
        this.desc = desc;
        this.pullMode = pullMode;
        this.pushMode = pushMode;
        this.supplier = supplier;
    }
}
