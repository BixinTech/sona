package cn.bixin.sona.web.request;

import java.io.Serializable;

public class MuteUserRequest implements Serializable {

    private static final long serialVersionUID = -4815246254981291613L;
    private String roomId;
    private String uid;
    private String targetUid;
    private Integer minute;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTargetUid() {
        return targetUid;
    }

    public void setTargetUid(String targetUid) {
        this.targetUid = targetUid;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }
}
