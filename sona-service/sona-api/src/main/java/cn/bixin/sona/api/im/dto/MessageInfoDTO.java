package cn.bixin.sona.api.im.dto;

import cn.bixin.sona.common.annotation.Description;

import java.io.Serializable;

public class MessageInfoDTO implements Serializable {

    private static final long serialVersionUID = 134770155459591601L;
    @Description("uid")
    private Long uid;

    @Description("房间号")
    private Long roomId;

    @Description("发送时间")
    private Long sendTime;

    @Description("消息内容")
    private String content;

    @Description("业务方类型")
    private String productCode;

    @Description("消息id")
    private String messageId;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
