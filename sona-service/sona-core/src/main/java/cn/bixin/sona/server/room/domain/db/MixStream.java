package cn.bixin.sona.server.room.domain.db;

import java.util.Date;

public class MixStream {
    private Long id;

    private Long roomId;

    private String bizRoomId;

    private String streamId;

    private Integer source;

    private String rtmpUrl;

    private String hlsUrl;

    private String hdlUrl;

    private String inputStream;

    private Integer status;

    private Date beginTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getBizRoomId() {
        return bizRoomId;
    }

    public void setBizRoomId(String bizRoomId) {
        this.bizRoomId = bizRoomId == null ? null : bizRoomId.trim();
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId == null ? null : streamId.trim();
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getRtmpUrl() {
        return rtmpUrl;
    }

    public void setRtmpUrl(String rtmpUrl) {
        this.rtmpUrl = rtmpUrl == null ? null : rtmpUrl.trim();
    }

    public String getHlsUrl() {
        return hlsUrl;
    }

    public void setHlsUrl(String hlsUrl) {
        this.hlsUrl = hlsUrl == null ? null : hlsUrl.trim();
    }

    public String getHdlUrl() {
        return hdlUrl;
    }

    public void setHdlUrl(String hdlUrl) {
        this.hdlUrl = hdlUrl == null ? null : hdlUrl.trim();
    }

    public String getInputStream() {
        return inputStream;
    }

    public void setInputStream(String inputStream) {
        this.inputStream = inputStream == null ? null : inputStream.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}