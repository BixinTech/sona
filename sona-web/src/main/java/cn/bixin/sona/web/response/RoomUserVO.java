package cn.bixin.sona.web.response;

import java.io.Serializable;

public class RoomUserVO implements Serializable {

    private String uid;
    /**
     * 角色：0-成员，4-管理员，5-房主
     */
    private int role;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
