package cn.bixin.sona.web.request;

import java.io.Serializable;
import java.util.Map;

public class EnterRequest implements Serializable {

    private static final long serialVersionUID = -938932330432571525L;
    private String roomId;
    private String uid;
    private String password;
    private Map<String, String> extMap;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, String> getExtMap() {
        return extMap;
    }

    public void setExtMap(Map<String, String> extMap) {
        this.extMap = extMap;
    }
}
