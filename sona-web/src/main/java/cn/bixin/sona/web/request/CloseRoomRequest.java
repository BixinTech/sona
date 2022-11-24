package cn.bixin.sona.web.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class CloseRoomRequest implements Serializable {

    @NotNull
    private String roomId;

    @NotNull
    private String uid;

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

}
