package cn.bixin.sona.gateway.common;

/**
 * @author qinwei
 */
public enum ChannelTypeEnum {

    GENERAL(0, "通用连接"),

    WHITEBOARD(1, "白板连接"),

    CHATROOM(2, "房间连接"),

    SIGNAL(3, "RTC信令连接"),

    ACTIVITY(4, "活动连接");

    private int type;

    private String desc;

    ChannelTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

}
