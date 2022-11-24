package cn.bixin.sona.web.response;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class StreamConfigInfoVO implements Serializable {
    private static final long serialVersionUID = -4182085795460285797L;

    /**
     * 流供应商 ZEGO TECENT WANGSU
     */
    private String supplier;
    /**
     * 流类型 AUDIO|VIDE
     */
    private String type;
    /**
     * 推流模式 SINGLE|MULTI
     */
    private String pushMode;
    /**
     * 拉流模式 SINGLE|MULTI|MIXED
     */
    private String pullMode;
    /**
     * 在线流列表 多路拉流时返回
     */
    private List<String> streamList;
    /**
     * 拉流url 单流或混流时返回
     */
    private String streamUrl;
    /**
     * 混流时的streamId
     */
    private String streamId;
    /**
     * 腾讯混流audioToken
     */
    private String audioToken;
    /**
     * 客户端注册音视频房间id
     */
    private Map<String, String> streamRoomId;
    /**
     * 切换扬声器
     */
    private String switchSpeaker;
    /**
     * 视频拉流地址
     */
    private List<SupplierVideoStreamVO> pullStreamInfoList;
    /**
     * appId配置信息
     */
    private AppInfoVO appInfo;
    /**
     * 播放器类型 1: 三方 2: 自建
     */
    private Integer playerType;
    /**
     * 码率
     */
    private Integer bitrate;

    public Integer getPlayerType() {
        return playerType;
    }

    public void setPlayerType(Integer playerType) {
        this.playerType = playerType;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPushMode() {
        return pushMode;
    }

    public void setPushMode(String pushMode) {
        this.pushMode = pushMode;
    }

    public String getPullMode() {
        return pullMode;
    }

    public void setPullMode(String pullMode) {
        this.pullMode = pullMode;
    }

    public List<String> getStreamList() {
        return streamList;
    }

    public void setStreamList(List<String> streamList) {
        this.streamList = streamList;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getAudioToken() {
        return audioToken;
    }

    public void setAudioToken(String audioToken) {
        this.audioToken = audioToken;
    }

    public Map<String, String> getStreamRoomId() {
        return streamRoomId;
    }

    public void setStreamRoomId(Map<String, String> streamRoomId) {
        this.streamRoomId = streamRoomId;
    }

    public String getSwitchSpeaker() {
        return switchSpeaker;
    }

    public void setSwitchSpeaker(String switchSpeaker) {
        this.switchSpeaker = switchSpeaker;
    }

    public List<SupplierVideoStreamVO> getPullStreamInfoList() {
        return pullStreamInfoList;
    }

    public void setPullStreamInfoList(List<SupplierVideoStreamVO> pullStreamInfoList) {
        this.pullStreamInfoList = pullStreamInfoList;
    }

    public AppInfoVO getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfoVO appInfo) {
        this.appInfo = appInfo;
    }
}
