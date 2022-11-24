package cn.bixin.sona.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class RoomDTO implements Serializable {

    /**
     * 房间ID
     */
    private Long roomId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 房间名称
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * '状态：0-关闭，1-打开'
     */
    private int status;

    /**
     * 用户UID
     */
    private long uid;

    /**
     * 产品号
     */
    private String productCode;

    /**
     * ext
     */
    private Map<String, Object> ext;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Map<String, Object> getExt() {
        return ext;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }
}
