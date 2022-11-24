package cn.bixin.sona.dto;

import java.io.Serializable;
import java.util.Date;

public class RoomConfigDTO implements Serializable {

    private long roomId;
    private String productCode;
    private String imModule;

    /**
     * 发送类型  长连 短连
     * persistantCon,nonPersistantCon
     */
    private int imSendType;

    private String streamSupplier;
    private String type;
    private String pullMode;
    private String pushMode;
    private boolean enterNotifySwitch;
    private boolean checkAdmin;
    private boolean needReplay;
    private int clientType;
    /**
     * 码率
     */
    private Integer bitrate;

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getImModule() {
        return imModule;
    }

    public void setImModule(String imModule) {
        this.imModule = imModule;
    }

    public int getImSendType() {
        return imSendType;
    }

    public void setImSendType(int imSendType) {
        this.imSendType = imSendType;
    }

    public String getStreamSupplier() {
        return streamSupplier;
    }

    public void setStreamSupplier(String streamSupplier) {
        this.streamSupplier = streamSupplier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPullMode() {
        return pullMode;
    }

    public void setPullMode(String pullMode) {
        this.pullMode = pullMode;
    }

    public String getPushMode() {
        return pushMode;
    }

    public void setPushMode(String pushMode) {
        this.pushMode = pushMode;
    }

    public boolean isEnterNotifySwitch() {
        return enterNotifySwitch;
    }

    public void setEnterNotifySwitch(boolean enterNotifySwitch) {
        this.enterNotifySwitch = enterNotifySwitch;
    }

    public boolean isCheckAdmin() {
        return checkAdmin;
    }

    public void setCheckAdmin(boolean checkAdmin) {
        this.checkAdmin = checkAdmin;
    }

    public boolean isNeedReplay() {
        return needReplay;
    }

    public void setNeedReplay(boolean needReplay) {
        this.needReplay = needReplay;
    }

    public int getClientType() {
        return clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }
}
