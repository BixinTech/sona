package cn.bixin.sona.api.im.request;

import cn.bixin.sona.api.im.enums.MsgFormatEnum;
import cn.bixin.sona.common.annotation.Description;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 客户端发送消息的格式
 *
 * @author qinwei
 */
public class ChatroomMessageRequest implements Serializable {

    private static final long serialVersionUID = 7718754664815537037L;

    @NotNull
    @Description("房间id")
    private Long roomId;

    @NotNull
    @Description("uid")
    private Long uid;

    @Description("消息优先级")
    private int priority;

    @Description("消息内容")
    private String content;

    /**
     * @see MsgFormatEnum
     */
    @Description("消息类型")
    private int msgFormat;

    @Description("消息id")
    private String messageId;

    @Description("是否需要保存")
    private boolean needToSave;

    @Description("业务消息类型")
    private String msgType;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMsgFormat() {
        return msgFormat;
    }

    public void setMsgFormat(int msgFormat) {
        this.msgFormat = msgFormat;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public boolean isNeedToSave() {
        return needToSave;
    }

    public void setNeedToSave(boolean needToSave) {
        this.needToSave = needToSave;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
