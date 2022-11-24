package cn.bixin.sona.server.room.domain.db;

import java.util.Date;

public class MixStreamReplay {

    private Long id;

    private Long roomId;

    private String bizRoomId;

    private String streamId;

    private Integer source;

    private String replayUrl;

    private Date beginTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;

    public static MixStreamReplay converter(MixStream mixStream, String replayUrl, Date beginTime, Date endTime){
        MixStreamReplay replay = new MixStreamReplay();
        replay.setRoomId(mixStream.getRoomId());
        replay.setBizRoomId(mixStream.getBizRoomId());
        replay.setStreamId(mixStream.getStreamId());
        replay.setSource(mixStream.getSource());
        replay.setReplayUrl(replayUrl);
        replay.setBeginTime(beginTime);
        replay.setEndTime(endTime);
        return replay;
    }

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
        this.bizRoomId = bizRoomId;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
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
