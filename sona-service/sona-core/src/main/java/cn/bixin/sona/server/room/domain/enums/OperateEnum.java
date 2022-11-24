package cn.bixin.sona.server.room.domain.enums;

public enum OperateEnum {

    SET(1, "设置"),
    CANCEL(0, "取消"),
    ;

    private int code;
    private String desc;

    OperateEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }
}
