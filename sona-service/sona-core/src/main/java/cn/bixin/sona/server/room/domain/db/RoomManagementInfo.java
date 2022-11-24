package cn.bixin.sona.server.room.domain.db;

import cn.bixin.sona.server.room.domain.request.OperateUserRequest;

import java.util.Calendar;
import java.util.Date;

public class RoomManagementInfo {

    private long roomId;
    private long uid;
    private int status;
    private Date expireTime;
    private int type;

    public static RoomManagementInfo wrapNewObj(long roomId, long uid, int type) {
        RoomManagementInfo managementInfo = new RoomManagementInfo();
        managementInfo.setRoomId(roomId);
        managementInfo.setUid(uid);
        managementInfo.setStatus(1);
        managementInfo.setType(type);
        return managementInfo;
    }

    public static RoomManagementInfo wrapNewObj(OperateUserRequest request, int type) {
        RoomManagementInfo managementInfo = new RoomManagementInfo();
        managementInfo.setRoomId(request.getRoomId());
        managementInfo.setUid(request.getTargetUid());
        managementInfo.setStatus(1);
        managementInfo.setType(type);
        return managementInfo;
    }

    public static RoomManagementInfo wrapNewObj(long roomId, long uid, int type, int minute) {
        RoomManagementInfo managementInfo = new RoomManagementInfo();
        managementInfo.setRoomId(roomId);
        managementInfo.setUid(uid);
        managementInfo.setStatus(1);
        managementInfo.setExpireTime(addMinute(minute));
        managementInfo.setType(type);
        return managementInfo;
    }

    /**
     * 当前时间加 minute分钟
     */
    private static Date addMinute(int minute) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, minute);
        return cal.getTime();
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
