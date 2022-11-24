package cn.bixin.sona.web.request.callback.zego;

import java.io.Serializable;

/**
 * @author dzsb-002293
 */
public class ZegoSaveReplayRequest implements Serializable {

    private static final long serialVersionUID = -6751979530852199829L;

    private String appid;
    private String stream_alias;
    private String replay_url;
    private int begin_time;
    private int end_time;
    private String extra_params;
    private int timestamp;
    private String nonce;
    private String signature;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getStream_alias() {
        return stream_alias;
    }

    public void setStream_alias(String stream_alias) {
        this.stream_alias = stream_alias;
    }

    public String getReplay_url() {
        return replay_url;
    }

    public void setReplay_url(String replay_url) {
        this.replay_url = replay_url;
    }

    public int getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(int begin_time) {
        this.begin_time = begin_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public String getExtra_params() {
        return extra_params;
    }

    public void setExtra_params(String extra_params) {
        this.extra_params = extra_params;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
