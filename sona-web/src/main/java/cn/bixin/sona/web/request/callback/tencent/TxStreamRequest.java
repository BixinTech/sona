package cn.bixin.sona.web.request.callback.tencent;

import java.io.Serializable;

/**
 * @author dzsb-002293
 */
public class TxStreamRequest implements Serializable {

    private static final long serialVersionUID = 8027002789374745753L;

    private int appid;
    private String app;
    private String appname;
    private String streamId;
    private String channelId;
    private long eventTime;
    private String sequence;
    private String node;
    private String userIp;
    private String streamParam;
    private String pushDuration;
    private int errcode;
    private String errmsg;

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getStreamParam() {
        return streamParam;
    }

    public void setStreamParam(String streamParam) {
        this.streamParam = streamParam;
    }

    public String getPushDuration() {
        return pushDuration;
    }

    public void setPushDuration(String pushDuration) {
        this.pushDuration = pushDuration;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
