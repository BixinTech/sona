package cn.bixin.sona.request;

import java.io.Serializable;

public class ChangeStreamRequest implements Serializable {

    private String streamId;
    private long roomId;
    private long uid;

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

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
