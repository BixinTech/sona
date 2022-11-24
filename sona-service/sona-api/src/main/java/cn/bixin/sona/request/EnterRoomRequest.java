package cn.bixin.sona.request;

import cn.bixin.sona.common.annotation.Description;
import cn.bixin.sona.enums.UserTypeEnum;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

public class EnterRoomRequest implements Serializable {

    @NotNull
    @Description("房间id")
    private long roomId;

    @NotNull
    @Description("用户uid")
    private long uid;

    @Description("房间密码")
    private String password;

    @Description("普通用户或vip用户")
    private UserTypeEnum userTypeEnum = UserTypeEnum.NORMAL;

    @Description("ext内容")
    private Map<String, String> extMap;

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserTypeEnum getUserTypeEnum() {
        return userTypeEnum;
    }

    public void setUserTypeEnum(UserTypeEnum userTypeEnum) {
        this.userTypeEnum = userTypeEnum;
    }

    public Map<String, String> getExtMap() {
        return extMap;
    }

    public void setExtMap(Map<String, String> extMap) {
        this.extMap = extMap;
    }
}
