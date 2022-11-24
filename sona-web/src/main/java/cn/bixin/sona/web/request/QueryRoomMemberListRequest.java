package cn.bixin.sona.web.request;

import java.io.Serializable;


public class QueryRoomMemberListRequest implements Serializable {

    private static final long serialVersionUID = -4301472685555749406L;
    private String roomId;
    private String anchor;
    private int limit;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
