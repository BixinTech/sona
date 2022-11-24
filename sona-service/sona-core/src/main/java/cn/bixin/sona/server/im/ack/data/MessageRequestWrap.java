package cn.bixin.sona.server.im.ack.data;

import cn.bixin.sona.api.im.request.RoomMessageRequest;

/**
 * @author qinwei
 */
public class MessageRequestWrap {

    private RoomMessageRequest request;

    private int retryCount;

    public RoomMessageRequest getRequest() {
        return request;
    }

    public void setRequest(RoomMessageRequest request) {
        this.request = request;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
