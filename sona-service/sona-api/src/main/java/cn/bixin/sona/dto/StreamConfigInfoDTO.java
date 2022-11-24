package cn.bixin.sona.dto;

import cn.bixin.sona.common.annotation.Description;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class StreamConfigInfoDTO implements Serializable {

    @Description("流供应商 ZEGO TECENT WANGSU")
    private String supplier;
    @Description("流类型 AUDIO|VIDEO")
    private String type;
    @Description("推流模式 SINGLE|MULTI")
    private String pushMode;
    @Description("拉流模式 SINGLE|MULTI|MIXED")
    private String pullMode;
    @Description("在线流列表 多路拉流时返回")
    private List<String> streamList;
    @Description("拉流url 单流或混流时返回")
    private String streamUrl;
    @Description("混流时的streamId")
    private String streamId;
    @Description("腾讯混流audioToken")
    private String audioToken;
    @Description("客户端注册音视频房间id")
    private Map<String, String> streamRoomId;
    @Description("切换扬声器")
    private String switchSpeaker;
    @Description("appId配置信息")
    private AppInfoDTO appInfo;
    @Description("码率")
    private Integer bitrate;

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

    public AppInfoDTO getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfoDTO appInfo) {
        this.appInfo = appInfo;
    }
}
