package cn.bixin.sona.server.room.domain.db;

import cn.bixin.sona.enums.RoomMixedEnum;
import org.springframework.beans.BeanUtils;

import java.util.Date;

public class ProductConfig {

    private Long id;
    private Date createTime;
    private Date updateTime;
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
    /**
     * 短码
     */
    private String shortCode;

    public static ProductConfig copy(ProductConfig productConfig) {
        ProductConfig result = new ProductConfig();
        BeanUtils.copyProperties(productConfig, result);

        return result;
    }

    public static ProductConfig convertConfigInfo(RoomConfig roomConfig, String productCode) {
        ProductConfig productConfig = new ProductConfig();
        BeanUtils.copyProperties(roomConfig, productConfig);
        productConfig.setProductCode(productCode);

        return productConfig;
    }

    public static void fillProductConfig(RoomMixedEnum mixed, ProductConfig productConfig) {
        if (productConfig == null) return ;
        productConfig.setStreamSupplier(mixed.getSupplier());
        productConfig.setPullMode(mixed.getPullMode());
        productConfig.setPushMode(mixed.getPushMode());

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
}
