package cn.bixin.sona.server.room.domain.db;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * 混流配置表
 *
 * @author 木欣
 * @create 2021-05-27
 * @email baojiawei@bixin.cn
 * create by intelliJ IDEA
 */
public class MixConfig implements Serializable {
    private static final long serialVersionUID = 1449637767939529176L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 房间ID
     */
    private Long roomId;
    /**
     * 用户uid
     */
    private String uid;
    /**
     * 画布宽
     */
    private Integer width;
    /**
     * 画布高
     */
    private Integer height;
    /**
     * 上边框
     */
    private Integer tops;
    /**
     * 下边框
     */
    private Integer bottom;
    /**
     * 左边框
     */
    private Integer lefts;
    /**
     * 右边框
     */
    private Integer rights;
    /**
     * 码率
     */
    private Integer bitrate;
    /**
     * 帧率
     */
    private Integer fps;
    /**
     * 记录状态 1: 视频 2: 音频
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getTops() {
        return tops;
    }

    public void setTops(Integer tops) {
        this.tops = tops;
    }

    public Integer getBottom() {
        return bottom;
    }

    public void setBottom(Integer bottom) {
        this.bottom = bottom;
    }

    public Integer getLefts() {
        return lefts;
    }

    public void setLefts(Integer lefts) {
        this.lefts = lefts;
    }

    public Integer getRights() {
        return rights;
    }

    public void setRights(Integer rights) {
        this.rights = rights;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public Integer getFps() {
        return fps;
    }

    public void setFps(Integer fps) {
        this.fps = fps;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
