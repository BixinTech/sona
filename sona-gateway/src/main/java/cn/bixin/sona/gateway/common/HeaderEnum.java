package cn.bixin.sona.gateway.common;

/**
 * @author qinwei
 */
public enum HeaderEnum {

    COMPRESS(1, "Body被压缩"),
    CHATROOM(2, "聊天室房间header"),
    BATCH(3, "批量"),
    AUTH(4, "鉴权");

    private int type;

    private String desc;

    HeaderEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

}
