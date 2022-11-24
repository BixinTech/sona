package cn.bixin.sona.server.room.domain.enums;

public enum StreamChangeEnum {

    ADD(1, "新增流"),
    CLOSE(2, "关闭流"),
    ;
    private int type;
    private String desc;

    StreamChangeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static StreamChangeEnum getByType(int type) {
        for (StreamChangeEnum each : StreamChangeEnum.values()) {
            if (type == each.getType()) {
                return each;
            }
        }
        return null;
    }
}
