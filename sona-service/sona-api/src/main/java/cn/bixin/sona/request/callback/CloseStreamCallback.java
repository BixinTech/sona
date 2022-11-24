package cn.bixin.sona.request.callback;

import java.io.Serializable;

public class CloseStreamCallback implements Serializable {
    private static final long serialVersionUID = 4427843798201787018L;

    private String sdkAppId;
    private String streamId;
    private String bizRoomId;
    private String roomId;
    private int closeType;
    private String errMsg;
    private int source;

    public String getSdkAppId() {
        return sdkAppId;
    }

    public void setSdkAppId(String sdkAppId) {
        this.sdkAppId = sdkAppId;
    }

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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getCloseType() {
        return closeType;
    }

    public void setCloseType(int closeType) {
        this.closeType = closeType;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
