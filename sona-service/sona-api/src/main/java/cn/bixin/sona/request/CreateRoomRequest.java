package cn.bixin.sona.request;

import cn.bixin.sona.common.annotation.Description;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

public class CreateRoomRequest implements Serializable {

    @NotNull
    @Description("房主uid")
    private long uid;

    @Description("房间名")
    private String name;

    @NotNull
    @Description("产品code")
    private String productCode;

    @Description("房间密码")
    private String password;

    @Description("ext内容")
    private Map<String, Object> extMap;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Object> getExtMap() {
        return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
        this.extMap = extMap;
    }
}
