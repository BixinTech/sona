package cn.bixin.sona.web.request.callback.zego;

import java.io.Serializable;

/**
 * @author dzsb-002293
 */
public class ZegoCloseStreamRequest implements Serializable {

    private static final long serialVersionUID = -7901867766790364260L;

    private String appid;
    private String stream_sid;
    private String channel_id;
    private String stream_alias;
    private int type;
    private int timestamp;
    private String nonce;
    private String signature;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getStream_sid() {
        return stream_sid;
    }

    public void setStream_sid(String stream_sid) {
        this.stream_sid = stream_sid;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getStream_alias() {
        return stream_alias;
    }

    public void setStream_alias(String stream_alias) {
        this.stream_alias = stream_alias;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
