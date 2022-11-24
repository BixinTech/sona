package cn.bixin.sona.server.room.domain.enums;

public enum StreamSupplierEnum {

    ZEGO(1, "Z"),
    TENCENT(2, "T"),
    ;

    private int code;
    private String desc;

    StreamSupplierEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescBySupplier(String supplier) {

        for (StreamSupplierEnum each : StreamSupplierEnum.values()) {
            if (each.name().equals(supplier)) {
                return each.getDesc();
            }
        }

        return "";
    }

    public static StreamSupplierEnum getByName(String supplier) {

        for (StreamSupplierEnum each : StreamSupplierEnum.values()) {
            if (each.name().equals(supplier)) {
                return each;
            }
        }

        return null;
    }

    public static StreamSupplierEnum getByDesc(String desc) {
        for (StreamSupplierEnum each : StreamSupplierEnum.values()) {
            if (each.getDesc().equals(desc)) {
                return each;
            }
        }

        return null;
    }

    public static StreamSupplierEnum getByCode(int code) {
        for (StreamSupplierEnum each : StreamSupplierEnum.values()) {
            if (each.getCode() == code) {
                return each;
            }
        }

        return null;
    }
}
