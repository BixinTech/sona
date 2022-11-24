package cn.bixin.sona.server.room.domain.request;

import cn.bixin.sona.request.OperateRequest;
import cn.bixin.sona.server.room.domain.enums.RoomManagementEnum;

public class OperateUserRequest {
    private long roomId;
    private long operatorUid;
    private long targetUid;
    private RoomManagementEnum type;
    private int operate;  //1:设置 0:取消
    private int minutes;

    public static OperateUserRequest wrapOperationObj(OperateRequest request, RoomManagementEnum type) {
        OperateUserRequest operateUserRequest = new OperateUserRequest();
        operateUserRequest.setOperate(request.getOperate());
        operateUserRequest.setRoomId(request.getRoomId());
        operateUserRequest.setType(type);
        operateUserRequest.setTargetUid(request.getTargetUid());
        operateUserRequest.setOperatorUid(request.getOperatorUid());
        operateUserRequest.setMinutes(request.getMinutes());
        return operateUserRequest;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(long operatorUid) {
        this.operatorUid = operatorUid;
    }

    public long getTargetUid() {
        return targetUid;
    }

    public void setTargetUid(long targetUid) {
        this.targetUid = targetUid;
    }

    public RoomManagementEnum getType() {
        return type;
    }

    public void setType(RoomManagementEnum type) {
        this.type = type;
    }

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
