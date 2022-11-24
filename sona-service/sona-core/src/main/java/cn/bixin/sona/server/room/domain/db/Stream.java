package cn.bixin.sona.server.room.domain.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.Objects;

public class Stream {
    private String productCode;
    private String streamId;
    private long roomId;
    private int status; // -1:初始化 0:关闭 1:打开
    private long uid;
    private int source;
    private String rtmpUrl;
    private String hlsUrl;
    private String hdlUrl;
    private String picUrl;
    private String replayUrl;
    private Date beginTime;
    private Date closeTime;
    private Date endTime;
    private int closeType; //0-正常关闭 1-后台超时关闭 2-同一主播直播关闭之前没有关闭的流
    private String errMsg;
    private String ext;

    /**
     * 根据key获取扩展字段中的信息
     */
    public String getExtByKey(String key) {
        if (StringUtils.isEmpty(ext) || StringUtils.isEmpty(key)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(ext);
        return jsonObject.getString(key);
    }

    public Boolean getBooleanExtByKey(String key) {
        if (StringUtils.isEmpty(ext) || StringUtils.isEmpty(key)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(ext);
        return jsonObject.getBoolean(key);
    }

    public Long getLongExtByKey(String key) {
        if (StringUtils.isEmpty(ext) || StringUtils.isEmpty(key)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(ext);
        return jsonObject.getLongValue(key);
    }

    public Integer getIntegerExtByKey(String key) {
        if (StringUtils.isEmpty(ext) || StringUtils.isEmpty(key)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(ext);
        return jsonObject.getInteger(key);
    }

    public void putExt(String key, Object value) {
        if (StringUtils.isEmpty(key) || Objects.isNull(value)) {
            return;
        }
        JSONObject jsonObject;
        if (StringUtils.isEmpty(ext)) {
            jsonObject = new JSONObject();
        } else {
            jsonObject = JSON.parseObject(ext);
        }
        jsonObject.put(key, value);
        ext = jsonObject.toJSONString();
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getRtmpUrl() {
        return rtmpUrl;
    }

    public void setRtmpUrl(String rtmpUrl) {
        this.rtmpUrl = rtmpUrl;
    }

    public String getHlsUrl() {
        return hlsUrl;
    }

    public void setHlsUrl(String hlsUrl) {
        this.hlsUrl = hlsUrl;
    }

    public String getHdlUrl() {
        return hdlUrl;
    }

    public void setHdlUrl(String hdlUrl) {
        this.hdlUrl = hdlUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
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

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
