package cn.bixin.sona.server.room.domain.db;

import cn.bixin.sona.enums.RoomMixedEnum;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

public class RoomConfig {

    private Long roomId;
    private String imModule;
    /**
     * 发送类型 长连 短连
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
    private Integer clientType;
    private String productCode;
    /**
     * 码率
     */
    private Integer bitrate;
    private String shortCode;

    public static RoomConfig convert(ProductConfig productConfig) {
        RoomConfig roomConfig = new RoomConfig();
        roomConfig.setImModule(productConfig.getImModule());
        roomConfig.setStreamSupplier(productConfig.getStreamSupplier());
        roomConfig.setType(productConfig.getType());
        roomConfig.setPullMode(productConfig.getPullMode());
        roomConfig.setPushMode(productConfig.getPushMode());
        roomConfig.setEnterNotifySwitch(productConfig.isEnterNotifySwitch());
        roomConfig.setCheckAdmin(productConfig.isCheckAdmin());
        roomConfig.setNeedReplay(productConfig.isNeedReplay());
        roomConfig.setProductCode(productConfig.getProductCode());
        roomConfig.setImSendType(productConfig.getImSendType());
        roomConfig.setClientType(productConfig.getClientType());
        roomConfig.setBitrate(productConfig.getBitrate());
        roomConfig.setShortCode(productConfig.getShortCode());

        return roomConfig;
    }

    public static RoomConfig convertByRoomConfig(RoomConfig each, RoomMixedEnum roomMixedEnum) {
        RoomConfig roomConfig = new RoomConfig();
        BeanUtils.copyProperties(each, roomConfig);
        fillRoomConfig(roomMixedEnum, roomConfig);

        roomConfig.setBitrate(each.getBitrate());

        return roomConfig;
    }

    public static RoomConfig convertByProductConfig(ProductConfig productConfig, RoomMixedEnum roomMixedEnum) {
        RoomConfig roomConfig = convert(productConfig);
        fillRoomConfig(roomMixedEnum, roomConfig);
        fillAudioConfig(productConfig, roomConfig);

        return roomConfig;
    }

    private static void fillAudioConfig(ProductConfig productConfig, RoomConfig roomConfig) {
        if (Objects.isNull(roomConfig)) {
            return;
        }

        roomConfig.setBitrate(productConfig.getBitrate());
    }

    private static void fillRoomConfig(RoomMixedEnum mixed, RoomConfig roomConfig) {
        if (roomConfig == null) {
            return;
        }

        roomConfig.setStreamSupplier(mixed.getSupplier());
        roomConfig.setPullMode(mixed.getPullMode());
        roomConfig.setPushMode(mixed.getPushMode());
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
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

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
}
