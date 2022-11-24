package cn.bixin.sona.request;

import java.io.Serializable;
import java.util.List;

public class OperateBatchRequest implements Serializable {

    private long roomId;
    private long operatorUid;
    private List<Long> targetUids;
    private Integer minutes;
    private int operate;  //1:设置  0:取消

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

    public List<Long> getTargetUids() {
        return targetUids;
    }

    public void setTargetUids(List<Long> targetUids) {
        this.targetUids = targetUids;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }
}
