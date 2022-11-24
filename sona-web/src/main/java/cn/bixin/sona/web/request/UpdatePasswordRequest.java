package cn.bixin.sona.web.request;

import java.io.Serializable;

public class UpdatePasswordRequest implements Serializable {

    private static final long serialVersionUID = -202804198059103460L;
    private String roomId;
    private String uid;
    private String oldPassword;
    private String newPassword;

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
