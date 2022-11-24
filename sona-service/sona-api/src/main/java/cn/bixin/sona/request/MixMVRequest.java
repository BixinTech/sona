package cn.bixin.sona.request;

import java.io.Serializable;

/**
 * MV混流请求参数
 *
 */
public class MixMVRequest implements Serializable {
    private static final long serialVersionUID = 1875818712301207222L;

    /**
     * 房间ID
     */
    private Long roomId;
    /**
     * 用户uid
     */
    private Long uid;
    /**
     * 混流状态: 1: 开始 2: 停止
     */
    private Integer mixStatus;
    /**
     * 画面宽度
     */
    private Integer width;
    /**
     * 画面高
     */
    private Integer height;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getMixStatus() {
        return mixStatus;
    }

    public void setMixStatus(Integer mixStatus) {
        this.mixStatus = mixStatus;
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
