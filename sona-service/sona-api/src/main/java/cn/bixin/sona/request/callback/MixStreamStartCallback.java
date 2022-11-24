package cn.bixin.sona.request.callback;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MixStreamStartCallback implements Serializable {
    private static final long serialVersionUID = -6362062912783843316L;

    private String streamId;
    private String bizRoomId;
    //1-zego 2-tecent
    private int source;
    private List<String> rtmpUrls;
    private List<String> hlsUrls;
    private List<String> hdlUrls;
    private List<String> picUrls;
    private Date createTime;
    private List<String> inputStreamList;
    private boolean isMix;


    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getBizRoomId() {
        return bizRoomId;
    }

    public void setBizRoomId(String bizRoomId) {
        this.bizRoomId = bizRoomId;
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

    public List<String> getInputStreamList() {
        return inputStreamList;
    }

    public void setInputStreamList(List<String> inputStreamList) {
        this.inputStreamList = inputStreamList;
    }

    public boolean isMix() {
        return isMix;
    }

    public void setMix(boolean mix) {
        isMix = mix;
    }
}
