package cn.bixin.sona.server.room.domain.request;

import com.alibaba.fastjson.annotation.JSONField;

public class TencentStartMixRequest {

//{
//    "SdkAppId":1400282383,
//    "RoomId":125092924,
//    "OutputParams":{
//        "StreamId":"7bc6a00a16c84eb19efba9b017b1389c",
//        "PureAudioStream":1
//    },
//    "EncodeParams":{
//        "AudioSampleRate":48000,
//        "AudioBitrate":128,
//        "AudioChannels":2
//    },
//    "LayoutParams":{
//        "Template":0
//    }
//}
    @JSONField(name="SdkAppId")
    private long sdkAppId;
    @JSONField(name="RoomId")
    private long roomId;
    @JSONField(name="OutputParams")
    private OutputParams outputParams;
    @JSONField(name="EncodeParams")
    private EncodeParams encodeParams;
    @JSONField(name="LayoutParams")
    private LayoutParams layoutParams;

    public static class OutputParams {
        @JSONField(name="StreamId")
        private String streamId;
        @JSONField(name="PureAudioStream")
        private int pureAudioStream;

        public String getStreamId() {
            return streamId;
        }

        public void setStreamId(String streamId) {
            this.streamId = streamId;
        }

        public int getPureAudioStream() {
            return pureAudioStream;
        }

        public void setPureAudioStream(int pureAudioStream) {
            this.pureAudioStream = pureAudioStream;
        }
    }

    public static class EncodeParams {
        @JSONField(name="AudioSampleRate")
        private int audioSampleRate;
        @JSONField(name="AudioBitrate")
        private int audioBitrate;
        @JSONField(name="AudioChannels")
        private int audioChannels;
        @JSONField(name="VideoWidth")
        private int videoWidth;
        @JSONField(name="VideoHeight")
        private int videoHeight;
        @JSONField(name="VideoBitrate")
        private int videoBitrate;
        @JSONField(name="VideoFramerate")
        private int videoFramerate;
        @JSONField(name="VideoGop")
        private int videoGop;

        public int getAudioSampleRate() {
            return audioSampleRate;
        }

        public void setAudioSampleRate(int audioSampleRate) {
            this.audioSampleRate = audioSampleRate;
        }

        public int getAudioBitrate() {
            return audioBitrate;
        }

        public void setAudioBitrate(int audioBitrate) {
            this.audioBitrate = audioBitrate;
        }

        public int getAudioChannels() {
            return audioChannels;
        }

        public void setAudioChannels(int audioChannels) {
            this.audioChannels = audioChannels;
        }

        public int getVideoWidth() {
            return videoWidth;
        }

        public void setVideoWidth(int videoWidth) {
            this.videoWidth = videoWidth;
        }

        public int getVideoHeight() {
            return videoHeight;
        }

        public void setVideoHeight(int videoHeight) {
            this.videoHeight = videoHeight;
        }

        public int getVideoBitrate() {
            return videoBitrate;
        }

        public void setVideoBitrate(int videoBitrate) {
            this.videoBitrate = videoBitrate;
        }

        public int getVideoFramerate() {
            return videoFramerate;
        }

        public void setVideoFramerate(int videoFramerate) {
            this.videoFramerate = videoFramerate;
        }

        public int getVideoGop() {
            return videoGop;
        }

        public void setVideoGop(int videoGop) {
            this.videoGop = videoGop;
        }
    }

    public static class LayoutParams {
        @JSONField(name="Template")
        private int template;

        public int getTemplate() {
            return template;
        }

        public void setTemplate(int template) {
            this.template = template;
        }
    }

    public long getSdkAppId() {
        return sdkAppId;
    }

    public void setSdkAppId(long sdkAppId) {
        this.sdkAppId = sdkAppId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public OutputParams getOutputParams() {
        return outputParams;
    }

    public void setOutputParams(OutputParams outputParams) {
        this.outputParams = outputParams;
    }

    public EncodeParams getEncodeParams() {
        return encodeParams;
    }

    public void setEncodeParams(EncodeParams encodeParams) {
        this.encodeParams = encodeParams;
    }

    public LayoutParams getLayoutParams() {
        return layoutParams;
    }

    public void setLayoutParams(LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
    }
}
