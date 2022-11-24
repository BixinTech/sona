package cn.bixin.sona.console.domain.enums;

public enum HotSwitchStatusEnum {

    RUNNING(0,"运行中"),
    SUCCESS(1,"成功"),
    FAILED(2,"失败"),
    ;

    HotSwitchStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private int code;

    private String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public HotSwitchStatusEnum codeOf(int code) {
        for (HotSwitchStatusEnum each : HotSwitchStatusEnum.values()) {
            if(each.getCode() == code){
                return each;
            }
        }
        return null;
    }

}