package cn.bixin.sona.server.room.domain.db;

import cn.bixin.sona.request.LogReportRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class RoomLogRecord {

    private String roomId;
    private long uid;
    private String streamId;

    private int type;

    private int businessSide;

    private int appId;
    private String version;

    private String request;
    private String response;
    private boolean success;
    private String exception;
    private Date createTime;

    private int productId;

    private String osName;

    public static RoomLogRecord convertRoomLogRecord(LogReportRequest request) {
        if (request == null || request.getCode() == 0 || StringUtils.isBlank(request.getData())) {
            return null;
        }

        JSONObject jsonObject = JSONObject.parseObject(request.getData());

        RoomLogRecord roomLogRecord = new RoomLogRecord();
        roomLogRecord.setAppId(request.getAppId());
        roomLogRecord.setVersion(request.getVersion());
        roomLogRecord.setType(request.getCode());

        if (jsonObject.getInteger("code") != null) {
            jsonObject = jsonObject.getJSONObject("data");
        }

        roomLogRecord.setRoomId(jsonObject.getString("roomId"));
        roomLogRecord.setUid(jsonObject.getLongValue("uid"));
        roomLogRecord.setStreamId(jsonObject.getString("streamId"));
        roomLogRecord.setSuccess(request.getCode() > 0);
        roomLogRecord.setOsName(request.getOsName());
        roomLogRecord.setProductId(request.getProductId());

        String sdkCode = jsonObject.getString("sdkCode");
        String message = jsonObject.getString("message");

        JSONObject reportMsg = new JSONObject();
        if (StringUtils.isNotBlank(sdkCode)) {
            reportMsg.put("sdkCode", sdkCode);
        }
        if (StringUtils.isNotBlank(message)) {
            reportMsg.put("message", message);
        }
        if (!jsonObject.isEmpty()) {
            if (!roomLogRecord.isSuccess()) {
                roomLogRecord.setException(JSON.toJSONString(reportMsg));
            }else {
                roomLogRecord.setResponse(JSON.toJSONString(reportMsg));
            }
        }

        roomLogRecord.setCreateTime(new Date());
        return roomLogRecord;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBusinessSide() {
        return businessSide;
    }

    public void setBusinessSide(int businessSide) {
        this.businessSide = businessSide;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }
}
