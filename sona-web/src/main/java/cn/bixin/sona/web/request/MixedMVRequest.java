package cn.bixin.sona.web.request;

import java.io.Serializable;

public class MixedMVRequest implements Serializable {
    private static final long serialVersionUID = 4810713264179423329L;

    /**
     * 混流类型 1: 开始混流MV 2: 停止混流MV
     */
    private Integer mixStatus;

    /**
     * 房间ID
     */
    private String roomId;

    private String uid;

    /**
     * Width
     */
    private Integer width;

    /**
     * Height
     */
    private Integer height;

    public Integer getMixStatus() {
        return mixStatus;
    }

    public void setMixStatus(Integer mixStatus) {
        this.mixStatus = mixStatus;
    }

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

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
