package cn.bixin.sona.web.request.callback.zego;

import java.io.Serializable;
import java.util.List;

/**
 * @author dzsb-002293
 */
public class ZegoCreateStreamRequest implements Serializable {

    private static final long serialVersionUID = 7636897817055799582L;

    private String appid;
    private String stream_sid;
    private String channel_id;
    private String title;
    private String stream_alias;
    private String publish_id;
    private String publish_name;
    private List<String> rtmp_url;
    private List<String> hls_url;
    private List<String> hdl_url;
    private List<String> pic_url;
    private int create_time;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStream_alias() {
        return stream_alias;
    }

    public void setStream_alias(String stream_alias) {
        this.stream_alias = stream_alias;
    }

    public String getPublish_id() {
        return publish_id;
    }

    public void setPublish_id(String publish_id) {
        this.publish_id = publish_id;
    }

    public String getPublish_name() {
        return publish_name;
    }

    public void setPublish_name(String publish_name) {
        this.publish_name = publish_name;
    }

    public List<String> getRtmp_url() {
        return rtmp_url;
    }

    public void setRtmp_url(List<String> rtmp_url) {
        this.rtmp_url = rtmp_url;
    }

    public List<String> getHls_url() {
        return hls_url;
    }

    public void setHls_url(List<String> hls_url) {
        this.hls_url = hls_url;
    }

    public List<String> getHdl_url() {
        return hdl_url;
    }

    public void setHdl_url(List<String> hdl_url) {
        this.hdl_url = hdl_url;
    }

    public List<String> getPic_url() {
        return pic_url;
    }

    public void setPic_url(List<String> pic_url) {
        this.pic_url = pic_url;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
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
