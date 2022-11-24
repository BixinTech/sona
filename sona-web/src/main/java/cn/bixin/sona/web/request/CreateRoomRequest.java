package cn.bixin.sona.web.request;

import java.io.Serializable;
import java.util.Map;

public class CreateRoomRequest implements Serializable {

    private static final long serialVersionUID = -2784248520516424354L;

    private String roomTitle;
    private String productCode;
    private String password;
    private String uid;
    private Map<String, Object> extMap;

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Map<String, Object> getExtMap() {
        return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
        this.extMap = extMap;
    }
}
