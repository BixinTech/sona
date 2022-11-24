package cn.bixin.sona.session.enums;

import java.util.Arrays;

/**
 * 离开房间的原因
 *
 * @author qinwei
 */
public enum LeaveReason {

    BUSINESS(1, "业务层离开"),
    CHANNEL_CLOSE(2, "连接断开"),
    CHANNEL_INVALID(3, "连接信息过期失效");

    private int code;

    private String desc;

    LeaveReason(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static LeaveReason getByCode(int code) {
        return Arrays.stream(values()).filter(reason -> reason.getCode() == code).findFirst().orElse(null);
    }

    public int getCode() {
        return code;
    }

}
