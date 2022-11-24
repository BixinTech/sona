package cn.bixin.sona.server.room.domain.request;

import java.util.List;
import java.util.Map;

public class ZegoMixRequest {

    private String signature;
    private long timestamp = System.currentTimeMillis();
    private String id_name;
    private String live_channel;
    private int appid;
    private String output_bg_image;
    private int with_sound_level;
    private String output_bg_color;
    private String userdata;
    private int seq;
    private String task_id;
    private int version;
    private List<MixInput> MixInput;
    private List<MixOutput> MixOutput;
    private Map<String, Object> extra_params;


    public static class MixInput {
        private String url;
        private String stream_id;
        private int sound_level_id;
        private int content_control;
        private RectInfo rect;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getStream_id() {
            return stream_id;
        }

        public void setStream_id(String stream_id) {
            this.stream_id = stream_id;
        }

        public int getSound_level_id() {
            return sound_level_id;
        }

        public void setSound_level_id(int sound_level_id) {
            this.sound_level_id = sound_level_id;
        }

        public int getContent_control() {
            return content_control;
        }

        public void setContent_control(int content_control) {
            this.content_control = content_control;
        }

        public RectInfo getRect() {
            return rect;
        }

        public void setRect(RectInfo rect) {
            this.rect = rect;
        }
    }

    public static class MixOutput {
        private String stream_id;
        private String mixurl;
        private int bitrate;
        private int fps;
        private int height;
        private int width;
        private int audio_enc_id;
        private int audio_bitrate;
        private int audio_channel_cnt;
        private int encode_mode;
        private int encode_qua;

        public String getStream_id() {
            return stream_id;
        }

        public void setStream_id(String stream_id) {
            this.stream_id = stream_id;
        }

        public String getMixurl() {
            return mixurl;
        }

        public void setMixurl(String mixurl) {
            this.mixurl = mixurl;
        }

        public int getBitrate() {
            return bitrate;
        }

        public void setBitrate(int bitrate) {
            this.bitrate = bitrate;
        }

        public int getFps() {
            return fps;
        }

        public void setFps(int fps) {
            this.fps = fps;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getAudio_enc_id() {
            return audio_enc_id;
        }

        public void setAudio_enc_id(int audio_enc_id) {
            this.audio_enc_id = audio_enc_id;
        }

        public int getAudio_bitrate() {
            return audio_bitrate;
        }

        public void setAudio_bitrate(int audio_bitrate) {
            this.audio_bitrate = audio_bitrate;
        }

        public int getAudio_channel_cnt() {
            return audio_channel_cnt;
        }

        public void setAudio_channel_cnt(int audio_channel_cnt) {
            this.audio_channel_cnt = audio_channel_cnt;
        }

        public int getEncode_mode() {
            return encode_mode;
        }

        public void setEncode_mode(int encode_mode) {
            this.encode_mode = encode_mode;
        }

        public int getEncode_qua() {
            return encode_qua;
        }

        public void setEncode_qua(int encode_qua) {
            this.encode_qua = encode_qua;
        }
    }

    public static class RectInfo {
        private int top;
        private int left;
        private int bottom;
        private int right;
        private int layer;

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public int getBottom() {
            return bottom;
        }

        public void setBottom(int bottom) {
            this.bottom = bottom;
        }

        public int getRight() {
            return right;
        }

        public void setRight(int right) {
            this.right = right;
        }

        public int getLayer() {
            return layer;
        }

        public void setLayer(int layer) {
            this.layer = layer;
        }
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public String getOutput_bg_image() {
        return output_bg_image;
    }

    public void setOutput_bg_image(String output_bg_image) {
        this.output_bg_image = output_bg_image;
    }

    public int getWith_sound_level() {
        return with_sound_level;
    }

    public void setWith_sound_level(int with_sound_level) {
        this.with_sound_level = with_sound_level;
    }

    public String getOutput_bg_color() {
        return output_bg_color;
    }

    public void setOutput_bg_color(String output_bg_color) {
        this.output_bg_color = output_bg_color;
    }

    public String getUserdata() {
        return userdata;
    }

    public void setUserdata(String userdata) {
        this.userdata = userdata;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<MixInput> getMixInput() {
        return MixInput;
    }

    public void setMixInput(List<MixInput> mixInput) {
        MixInput = mixInput;
    }

    public List<MixOutput> getMixOutput() {
        return MixOutput;
    }

    public void setMixOutput(List<MixOutput> mixOutput) {
        MixOutput = mixOutput;
    }

    public Map<String, Object> getExtra_params() {
        return extra_params;
    }

    public void setExtra_params(Map<String, Object> extra_params) {
        this.extra_params = extra_params;
    }
}
