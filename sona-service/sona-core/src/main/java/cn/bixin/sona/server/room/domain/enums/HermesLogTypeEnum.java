package cn.bixin.sona.server.room.domain.enums;

public enum HermesLogTypeEnum {

    ROOM_CREATE("room_create", "创建房间"),
    ROOM_ENTER("room_enter", "进入房间"),
    ROOM_LEAVE("room_leave", "离开房间"),
    ROOM_CLOSE("room_close", "关闭房间"),
    ROOM_PASSWORD("room_password", "设置密码"),
    ROOM_KICK("room_kick", "房间踢人"),

    GAME_START("game_start", "开始游戏"),
    STREAM_PUSH("stream_push", "推流"),
    STREAM_MIX("stream_mix", "混流"),

    ;

    private String codeStr;
    private String desc;

    HermesLogTypeEnum(String codeStr, String desc){
        this.codeStr = codeStr;
        this.desc = desc;
    }

    public String getCodeStr() {
        return codeStr;
    }

    public String getDesc() {
        return desc;
    }
}
