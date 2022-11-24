package cn.bixin.sona.server.room.domain.db;

public class RoomManagementLog {

    private long roomId;
    private long uid;
    private int type;
    private int operate;  // 0: 取消 1: 设置
    private long operator;

    public static RoomManagementLog wrapRecordObj(long roomId, long targetUid, int type, int operate, long operator) {
        RoomManagementLog log = new RoomManagementLog();
        log.setRoomId(roomId);
        log.setUid(targetUid);
        log.setType(type);
        log.setOperate(operate);
        log.setOperator(operator);

        return log;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }

    public long getOperator() {
        return operator;
    }

    public void setOperator(long operator) {
        this.operator = operator;
    }
}
