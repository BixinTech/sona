package cn.bixin.sona.dto;

import cn.bixin.sona.common.annotation.Description;

import java.io.Serializable;

//todo 补充desription
public class AppInfoDTO implements Serializable {

    @Description("")
    private int appId;
    private String appSign;
    private String token;
    private String appID;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getAppID(){
        return appID;
    }

    public void setAppID(String appID){
        this.appID = appID;
    }

    public String getAppSign() {
        return appSign;
    }

    public void setAppSign(String appSign) {
        this.appSign = appSign;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
