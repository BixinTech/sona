package cn.bixin.sona.web.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class KickRequest implements Serializable {

    private static final long serialVersionUID = -1207120172170826953L;
    @NotNull
    private long roomId;

    @NotNull
    private long uid;

    @NotNull
    private long targetUid;

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getTargetUid() {
        return targetUid;
    }

    public void setTargetUid(long targetUid) {
        this.targetUid = targetUid;
    }
}
