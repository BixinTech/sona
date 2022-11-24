package cn.bixin.sona.api.socket.request;

import cn.bixin.sona.common.annotation.Description;

import java.io.Serializable;
import java.util.List;

/**
 * @author qinwei
 */
public class ChatroomMsgRequest implements Serializable {

    private static final long serialVersionUID = -4709497798143997685L;

    @Description("发送方uid")
    private String fromUid;

    @Description("聊天室id")
    private String roomId;

    @Description("内容")
    private String content;

    @Description("消息的发送时间ms")
    private long sendTime;

    @Description("是否高优先级消息")
    private boolean highPriority;

    @Description("消息id")
    private String messageId;

    @Description("需要ack的用户")
    private List<Long> ackUids;

    @Description("业务类型")
    private String productCode;

    public String getFromUid() {
        return fromUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public boolean isHighPriority() {
        return highPriority;
    }

    public void setHighPriority(boolean highPriority) {
        this.highPriority = highPriority;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public List<Long> getAckUids() {
        return ackUids;
    }

    public void setAckUids(List<Long> ackUids) {
        this.ackUids = ackUids;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}
