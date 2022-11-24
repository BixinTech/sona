package cn.bixin.sona.dto;

import java.io.Serializable;
import java.util.Map;

/**
 * 房间详情信息
 */
public class RoomDetailInfoDTO implements Serializable {

    private ProductConfigInfoDTO productConfig;
    private Long roomId;
    private Long ownerUid;
    private Long guestUid;
    private String nickname;
    private Map<String, Object> extra;

    public ProductConfigInfoDTO getProductConfig() {
        return productConfig;
    }

    public void setProductConfig(ProductConfigInfoDTO productConfig) {
        this.productConfig = productConfig;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public Long getGuestUid() {
        return guestUid;
    }

    public void setGuestUid(Long guestUid) {
        this.guestUid = guestUid;
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

    public Long getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(Long ownerUid) {
        this.ownerUid = ownerUid;
    }
}
