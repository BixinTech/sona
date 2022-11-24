package cn.bixin.sona.api.socket.request;

import cn.bixin.sona.common.annotation.Description;

import java.io.Serializable;

/**
 * @author qinwei
 */
public class GroupMsgRequest implements Serializable {

    private static final long serialVersionUID = -2681923503006604307L;

    @Description("群组id")
    private String groupId;

    @Description("消息内容")
    private String content;

    @Description("消息的发送时间ms")
    private long sendTime;

    @Description("群组的messageId")
    private String messageId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
