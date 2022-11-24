package cn.bixin.sona.dto;

import java.io.Serializable;

public class RoomUserDTO implements Serializable {

    private long uid;
    /**
     * 角色：0-成员，4-管理员，5-房主
     */
    private int role;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
