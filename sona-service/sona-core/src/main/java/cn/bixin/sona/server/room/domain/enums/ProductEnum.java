package cn.bixin.sona.server.room.domain.enums;


import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum ProductEnum {
    /**
     * 聊天室
     */
    CHATROOM("C"),
    /**
     * 直播
     */
    LIVING("L"),
    /**
     * 小游戏
     */
    GAME("G");

    private String code;

    ProductEnum(String code) {
        this.code = code;
    }

    public static ProductEnum getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return Arrays.stream(values()).filter(each -> each.getCode().equalsIgnoreCase(code)).findFirst().orElse(null);
    }

    public static ProductEnum getPlatformByName(String productCode) {
        if (StringUtils.isBlank(productCode)) {
            return null;
        }
        return Arrays.stream(values()).filter(each -> each.name().equalsIgnoreCase(productCode)).findFirst().orElse(null);
    }

    public String getCode() {
        return code;
    }
}
