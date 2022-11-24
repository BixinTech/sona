package cn.bixin.sona.web.request;

import java.io.Serializable;
import java.util.List;

public class MuteStreamRequest implements Serializable {

    private static final long serialVersionUID = 8411847580566413349L;
    private String roomId;
    private List<String> targetUids;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<String> getTargetUids() {
        return targetUids;
    }

    public void setTargetUids(List<String> targetUids) {
        this.targetUids = targetUids;
    }
}
