package cn.bixin.sona.server.room.domain.enums;

public enum RoomManagementEnum {
    //1拉黑、2禁言、3踢出 4、管理员 5、房主 6、禁止推流
    BLOCK(1),
    MUTE(2),
    KICK(3),
    ADMIN(4),
    OWNER(5),
    STREAM_PUSH_FORBID(6);

    private int code;

    RoomManagementEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
