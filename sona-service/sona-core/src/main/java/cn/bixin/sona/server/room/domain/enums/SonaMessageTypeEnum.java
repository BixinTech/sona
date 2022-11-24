package cn.bixin.sona.server.room.domain.enums;

/**
 * @author qinwei
 */

public enum SonaMessageTypeEnum {

    MSG_CREATE_ROOM("10000", "打开房间"),

    MSG_CLOSE_ROOM("10001", "关闭房间"),

    MSG_ENTER_ROOM("10002", "进入房间"),

    MSG_LEAVE_ROOM("10003", "离开房间"),

    MSG_ADMIN("10004", "管理员设置/取消"),

    MSG_BLOCK("10005", "拉黑设置/取消"),

    MSG_MUTE("10006", "禁言设置/取消"),

    MSG_KICK("10007", "踢人"),

    MSG_SWITCH_AUDIO("10008", "切换供应商"),

    MSG_VOICE("10009", "静音设置/取消");

    private String code;
    private String desc;

    SonaMessageTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

}
