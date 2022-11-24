package cn.bixin.sona.server.room.domain.db;

import cn.bixin.sona.common.enums.RoomStatus;
import cn.bixin.sona.request.CreateRoomRequest;
import cn.bixin.sona.server.room.domain.enums.StreamSupplierEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Room {
    private Long roomId;
    private String name;
    private Integer status;
    private String password;
    private Long uid;
    private String productCode;
    private String imModule;
    private Date createTime;
    private Date updateTime;
    private String resourceRoomId;
    private String ext;

    public static Room wrapCreateObj(CreateRoomRequest request, Long roomId, String imModule) {
        Room room = new Room();
        room.setRoomId(roomId);
        room.setName(StringUtils.isNotBlank(request.getName()) ? request.getName() : generateRoomTitle(request.getUid()));
        room.setStatus(RoomStatus.VALID.getCode());
        room.setPassword(request.getPassword());
        room.setUid(request.getUid());
        room.setProductCode(request.getProductCode());
        room.setImModule(imModule);
        room.setCreateTime(new Date());
        room.setExt(CollectionUtils.isEmpty(request.getExtMap()) ?
                JSONObject.toJSONString(initStreamExt(String.valueOf(roomId)))
                : genExtWithStream(request.getExtMap(), String.valueOf(roomId)));

        return room;
    }

    private static String generateRoomTitle(long uid) {
        return "room" + uid + System.currentTimeMillis();
    }

    private static String genExtWithStream(Map<String, Object> extMap, String roomIdStr) {
        Map<String, Object> result = Maps.newHashMap(extMap);
        result.putAll(initStreamExt(roomIdStr));
        return JSON.toJSONString(result);
    }

    private static Map<String, Object> initStreamExt(String roomIdStr) {
        return new HashMap<String, Object>() {{
            put(StreamSupplierEnum.ZEGO.name(), roomIdStr);
            put(StreamSupplierEnum.TENCENT.name(), roomIdStr);
        }};
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getImModule() {
        return imModule;
    }

    public void setImModule(String imModule) {
        this.imModule = imModule;
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

    public String getResourceRoomId() {
        return resourceRoomId;
    }

    public void setResourceRoomId(String resourceRoomId) {
        this.resourceRoomId = resourceRoomId;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
