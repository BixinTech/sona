package cn.bixin.sona.common.enums;

public enum RoomStatus {

    INVALID(0),
    VALID(1);

    private final int code;

    RoomStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
