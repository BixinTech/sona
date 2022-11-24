package cn.bixin.sona.web.response;

import java.io.Serializable;

public class MessageInfoVO implements Serializable {
    private static final long serialVersionUID = 1136871631102786641L;

    /**
     * 发送方uid
     */
    private String uid;
    /**
     * 房间id
     */
    private String roomId;
    /**
     * 消息发送时间戳（毫秒）
     */
    private Long sendTime;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 产品code
     */
    private String productCode;
    /**
     * 消息id
     */
    private String messageId;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
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
