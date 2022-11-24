package cn.bixin.sona.request;

import cn.bixin.sona.common.annotation.Description;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class OpenCloseRoomRequest implements Serializable {

    @NotNull
    @Description("用户uid")
    private long uid;

    @NotNull
    @Description("房间id")
    private long roomId;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }
}
