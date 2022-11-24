package cn.bixin.sona.request;

import java.io.Serializable;

public class UpdatePasswordRequest implements Serializable {

    private long roomId;
    private long operatorUid;
    private String oldPassword;
    private String newPassword;

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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
