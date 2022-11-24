package cn.bixin.sona.api.im.enums;


/**
 * 消息类型枚举
 *
 * @author qinwei
 */
public enum MsgFormatEnum {

    BUSINESS(100, "业务消息"),
    TEXT(101, "文本消息"),
    PIC(102, "图片消息"),
    EMOJI(103, "表情消息"),
    AUDIO(104, "语音消息"),
    VIDEO(105, "视频消息"),
    ACK(106, "ack消息");

    private int code;

    private String desc;

    MsgFormatEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }
}
