package cn.bixin.sona.request;

import java.io.Serializable;

public class InitStreamRequest implements Serializable {

    private long roomId;
    private long uid;

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
}
