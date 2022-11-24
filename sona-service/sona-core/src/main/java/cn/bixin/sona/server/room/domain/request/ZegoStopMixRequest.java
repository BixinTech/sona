package cn.bixin.sona.server.room.domain.request;

public class ZegoStopMixRequest {
    private int appid;
    private long timestamp = System.currentTimeMillis();
    private String signature;
    private String id_name;
    private String live_channel;
    private String mixurl;
    private String stream_id;
    private int seq;

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getId_name() {
        return id_name;
    }

    public void setId_name(String id_name) {
        this.id_name = id_name;
    }

    public String getLive_channel() {
        return live_channel;
    }

    public void setLive_channel(String live_channel) {
        this.live_channel = live_channel;
    }

    public String getMixurl() {
        return mixurl;
    }

    public void setMixurl(String mixurl) {
        this.mixurl = mixurl;
    }

    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
