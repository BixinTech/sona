package cn.bixin.sona.api.im.request;

import cn.bixin.sona.common.annotation.Description;

/**
 * @author qinwei
 */
public class MessageRequest {

    @Description("发送者uid")
    private Long uid;

    @Description("消息内容")
    private String content;

    @Description("发送时间")
    private Long sendTime = System.currentTimeMillis();

    @Description("消息id")
    private String messageId;

    @Description("业务类型")
    private String productCode;

    @Description("是否要保存消息")
    private boolean needToSave;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public boolean isNeedToSave() {
        return needToSave;
    }

    public void setNeedToSave(boolean needToSave) {
        this.needToSave = needToSave;
    }
}
