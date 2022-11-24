package cn.bixin.sona.web.response;

import java.io.Serializable;
import java.util.List;

import cn.bixin.sona.common.annotation.Description;

public class ImConfigInfoVO implements Serializable {
    private static final long serialVersionUID = 2105997091594344942L;
    @Description("IM模块  CHATROOM, GROUP")
    private String module;

    @Description("消息必达开关")
    private boolean arrivalMessageSwitch;

    @Description("客户端消息队列大小")
    private int clientQueueSize;

    @Description("消息过期时间")
    private long messageExpireTime;

    /**
     * 发送类型  长连 短连
     * 1 短连,2 长连
     */
    @Description("发送类型  长连 短连")
    private int imSendType;



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
