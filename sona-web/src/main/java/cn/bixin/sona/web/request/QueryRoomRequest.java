package cn.bixin.sona.web.request;

import java.io.Serializable;


public class QueryRoomRequest implements Serializable {

    private static final long serialVersionUID = -95548654907234625L;
    private String roomId;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
