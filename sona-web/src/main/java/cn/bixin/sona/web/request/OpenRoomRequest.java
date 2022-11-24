package cn.bixin.sona.web.request;

import java.io.Serializable;

public class OpenRoomRequest implements Serializable {

    private static final long serialVersionUID = -5389005922633102223L;
    private String roomId;
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
