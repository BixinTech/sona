package cn.bixin.sona.web.response;


import java.io.Serializable;
import java.util.Map;

public class RoomDetailInfoVO implements Serializable {
    private static final long serialVersionUID = 6821102413465632376L;

    private ProductConfigInfoVO productConfig;
    private String roomId;
    private String ownerUid;
    private String guestUid;
    private String addr;
    private String nickname;
    private Map<String, Object> extra;


    public ProductConfigInfoVO getProductConfig() {
        return productConfig;
    }

    public void setProductConfig(ProductConfigInfoVO productConfig) {
        this.productConfig = productConfig;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }


    public String getGuestUid() {
        return guestUid;
    }

    public void setGuestUid(String guestUid) {
        this.guestUid = guestUid;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    public String getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(String ownerUid) {
        this.ownerUid = ownerUid;
    }
}
