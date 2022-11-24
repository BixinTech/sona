package cn.bixin.sona.request.callback;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CreateStreamCallback implements Serializable {
    private static final long serialVersionUID = 8871165605042724312L;

    private String streamId;
    private long roomId;
    private long uid;
    private String sdkAppId;
    private int appId;
    private int source; //1-zego 2-tecent
    private List<String> rtmpUrls;
    private List<String> hlsUrls;
    private List<String> hdlUrls;
    private List<String> picUrls;
    private Date createTime;

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public List<String> getRtmpUrls() {
        return rtmpUrls;
    }

    public void setRtmpUrls(List<String> rtmpUrls) {
        this.rtmpUrls = rtmpUrls;
    }

    public List<String> getHlsUrls() {
        return hlsUrls;
    }

    public void setHlsUrls(List<String> hlsUrls) {
        this.hlsUrls = hlsUrls;
    }

    public List<String> getHdlUrls() {
        return hdlUrls;
    }

    public void setHdlUrls(List<String> hdlUrls) {
        this.hdlUrls = hdlUrls;
    }

    public List<String> getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(List<String> picUrls) {
        this.picUrls = picUrls;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getSdkAppId() {
        return sdkAppId;
    }

    public void setSdkAppId(String sdkAppId) {
        this.sdkAppId = sdkAppId;
    }
}
