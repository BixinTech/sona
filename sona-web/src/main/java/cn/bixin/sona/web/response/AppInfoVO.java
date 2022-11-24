package cn.bixin.sona.web.response;

import java.io.Serializable;

public class AppInfoVO implements Serializable {

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

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }
}
