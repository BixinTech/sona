package cn.bixin.sona.api.im.enums;

import java.util.Arrays;

/**
 * 消息优先级
 *
 * @author qinwei
 */
public enum PriorityEnum {

    HIGH(1, "高优先级"),

    MEDIUM_HIGH(2, "中高优先级"),

    MEDIUM(3, "中优先级"),

    LOW(4, "低优先级");

    private final int level;

    private final String desc;

    PriorityEnum(Integer level, String desc) {
        this.level = level;
        this.desc = desc;
    }

    public static PriorityEnum getPriorityType(int level) {
        return Arrays.stream(values()).filter(type -> type.level == level).findFirst().orElse(LOW);
    }
}
