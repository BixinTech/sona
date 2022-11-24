package cn.bixin.sona.api.im.request;

import cn.bixin.sona.api.im.enums.PriorityEnum;
import cn.bixin.sona.common.annotation.Description;

import java.io.Serializable;
import java.util.List;

/**
 * 房间消息
 *
 * @author qinwei
 */
public class RoomMessageRequest extends MessageRequest implements Serializable {

    private static final long serialVersionUID = -7158670156891227061L;

    @Description("房间id")
    private Long roomId;

    @Description("业务消息类型")
    private String msgType;

    @Description("消息等级")
    private PriorityEnum priority = PriorityEnum.LOW;

    @Description("需要ack的用户")
    private List<Long> ackUids;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public PriorityEnum getPriority() {
        return priority;
    }

    public void setPriority(PriorityEnum priority) {
        this.priority = priority;
    }

    public List<Long> getAckUids() {
        return ackUids;
    }

    public void setAckUids(List<Long> ackUids) {
        this.ackUids = ackUids;
    }
}
