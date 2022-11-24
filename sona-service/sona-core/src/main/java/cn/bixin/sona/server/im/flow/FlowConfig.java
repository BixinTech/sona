package cn.bixin.sona.server.im.flow;

import cn.bixin.sona.common.annotation.Description;

import java.io.Serializable;

/**
 * @author qinwei
 */
public class FlowConfig implements Serializable {

    private static final long serialVersionUID = 1620850821171001347L;

    @Description("普通令牌数")
    private int capacity;

    @Description("高等级令牌数")
    private int highCapacity;

    @Description("需要的令牌数")
    private int request;

    @Description("扣减令牌数")
    private int deduct;

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getHighCapacity() {
        return highCapacity;
    }

    public void setHighCapacity(int highCapacity) {
        this.highCapacity = highCapacity;
    }

    public int getRequest() {
        return request;
    }

    public void setRequest(int request) {
        this.request = request;
    }

    public int getDeduct() {
        return deduct;
    }

    public void setDeduct(int deduct) {
        this.deduct = deduct;
    }
}
