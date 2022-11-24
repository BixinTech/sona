package cn.bixin.sona.web.request;

import java.io.Serializable;

public class BlockUserRequest implements Serializable {

    private String roomId;
    private String uid;
    private String targetUid;
    private String reason;

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
