package cn.bixin.sona.server.room.domain.db;

public class GroupMember {
    /**
     * 自增ID
     */
    private Long id;

    /**
     * 产品码
     */
    private String productCode;

    /**
     * 房间ID
     */
    private Long roomId;

    /**
     * 1在线，0离开
     */
    private int status;

    /**
     * 用户UID
     */
    private long uid;

    public static GroupMember wrapCreateObj(long roomId, long uid, String productCode) {
        GroupMember groupMember = new GroupMember();
        groupMember.setRoomId(roomId);
        groupMember.setUid(uid);
        groupMember.setProductCode(productCode);
        groupMember.setStatus(1);
        return groupMember;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
