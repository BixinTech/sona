package cn.bixin.sona.api.socket.request;

import cn.bixin.sona.common.annotation.Description;

import java.io.Serializable;
import java.util.List;

/**
 * @author qinwei
 */
public class BatchChatroomMsgRequest implements Serializable {

    private static final long serialVersionUID = -4999009303021262438L;

    @Description("聊天室ids")
    private List<String> roomIds;

    @Description("内容")
    private String content;

    @Description("消息的发送时间ms")
    private long sendTime;

    @Description("需要ack的用户")
    private List<Long> ackUids;

    @Description("消息id")
    private String messageId;

    @Description("业务类型")
    private String businessType;

    public List<String> getRoomIds() {
        return roomIds;
    }

    public void setRoomIds(List<String> roomIds) {
        this.roomIds = roomIds;
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

    public List<Long> getAckUids() {
        return ackUids;
    }

    public void setAckUids(List<Long> ackUids) {
        this.ackUids = ackUids;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}
