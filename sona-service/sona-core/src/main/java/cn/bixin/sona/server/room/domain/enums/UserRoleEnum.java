package cn.bixin.sona.server.room.domain.enums;

public enum UserRoleEnum {

    MEMBER(0),
    ADMIN(4),
    OWNER(5),

    ;

    private int code;

    UserRoleEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static UserRoleEnum getRoleByCode(int code) {
        for (UserRoleEnum each : UserRoleEnum.values()) {
            if (each.getCode() == code) {
                return each;
            }
        }

        return null;
    }
}
