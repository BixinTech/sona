package cn.bixin.sona.request.callback;

import java.io.Serializable;
import java.util.Date;

public class MixStreamEndCallback implements Serializable {
    private static final long serialVersionUID = -6362062912783843316L;

    private String streamId;
    private String bizRoomId;
    private String mixUrl;
    //1-zego 2-tecent
    private int source;
    private Date createTime;
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

    public String getMixUrl() {
        return mixUrl;
    }

    public void setMixUrl(String mixUrl) {
        this.mixUrl = mixUrl;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isMix() {
        return isMix;
    }

    public void setMix(boolean mix) {
        isMix = mix;
    }
}
