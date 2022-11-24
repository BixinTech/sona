package cn.bixin.sona.api.im.request;

import cn.bixin.sona.common.annotation.Description;

import java.io.Serializable;

public class MessageQueryRequest implements Serializable {
    private static final long serialVersionUID = -6362293992552763440L;

    @Description("房间ID")
    private long roomId;

    @Description("消息发送方uid（非必传）")
    private Long uid;

    @Description("是否倒叙查询")
    private boolean reserved;

    @Description("分页游标")
    private String anchor;

    @Description("分页大小")
    private int limit = 20;

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
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
