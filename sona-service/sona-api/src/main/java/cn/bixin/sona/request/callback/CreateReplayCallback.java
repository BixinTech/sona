package cn.bixin.sona.request.callback;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class CreateReplayCallback implements Serializable {
    private static final long serialVersionUID = -1056348280186472308L;

    private String sdkAppId;
    private String streamId;
    private String replayUrl;
    private Date beginTime;
    private Date endTime;
    private int source;
    private Map<String, Object> extraParams;

    public String getSdkAppId() {
        return sdkAppId;
    }

    public void setSdkAppId(String sdkAppId) {
        this.sdkAppId = sdkAppId;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getReplayUrl() {
        return replayUrl;
    }

    public void setReplayUrl(String replayUrl) {
        this.replayUrl = replayUrl;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Map<String, Object> getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(Map<String, Object> extraParams) {
        this.extraParams = extraParams;
    }
}
