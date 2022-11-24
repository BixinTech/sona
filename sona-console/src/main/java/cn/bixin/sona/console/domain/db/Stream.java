package cn.bixin.sona.console.domain.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.Objects;

@Data
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
    private long id;

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
}
