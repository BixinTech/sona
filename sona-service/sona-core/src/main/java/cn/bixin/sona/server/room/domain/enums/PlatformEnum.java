package cn.bixin.sona.server.room.domain.enums;

import cn.bixin.sona.common.annotation.Description;
import org.apache.commons.lang.StringUtils;

public enum PlatformEnum {
    @Description("无法识别")
    UN_KNOWN(0),

    @Description("android包")
    ANDROID(1),

    @Description("ios包")
    IOS(2),

    @Description("pc包")
    WINDOWS(3),

    @Description("微信小程序")
    WECHAT_APPLET(4),
    ;

    private int code;

    PlatformEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static PlatformEnum getPlatformByCode(int code) {
        for (PlatformEnum platform : values()) {
            if (platform.getCode() == code) {
                return platform;
            }
        }
        return PlatformEnum.UN_KNOWN;
    }

    public static PlatformEnum getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        for (PlatformEnum each : PlatformEnum.values()) {
            if (each.getCode() == Integer.valueOf(code)) {
                return each;
            }
        }

        return null;
    }
}
