package cn.bixin.sona.dto;

import cn.bixin.sona.common.annotation.Description;

import java.io.Serializable;

public class ImConfigInfoDTO implements Serializable {


    @Description("IM模块  CHATROOM, GROUP")
    private String module;

    @Description("IM发送类型 长连 短连")
    private int imSendType;

    @Description("消息必达开关")
    private boolean arrivalMessageSwitch;

    @Description("客户端消息队列大小")
    private int clientQueueSize = 600;

    @Description("客户端消息过期时间")
    private long messageExpireTime = 10000;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public boolean isArrivalMessageSwitch() {
        return arrivalMessageSwitch;
    }

    public void setArrivalMessageSwitch(boolean arrivalMessageSwitch) {
        this.arrivalMessageSwitch = arrivalMessageSwitch;
    }

    public int getClientQueueSize() {
        return clientQueueSize;
    }

    public void setClientQueueSize(int clientQueueSize) {
        this.clientQueueSize = clientQueueSize;
    }

    public long getMessageExpireTime() {
        return messageExpireTime;
    }

    public void setMessageExpireTime(long messageExpireTime) {
        this.messageExpireTime = messageExpireTime;
    }

    public void setImSendType(int imSendType){
        this.imSendType = imSendType;
    }

    public int getImSendType(){
        return imSendType;
    }
}
