package cn.bixin.sona.server.im.handler;

import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.common.dto.Response;

/**
 * @author qinwei
 */
public interface ChatRoomHandler {

    /**
     * 处理请求
     */
    Response<Boolean> handle(RoomMessageRequest request);

    /**
     * 是否支持处理
     */
    default boolean support(RoomMessageRequest request) {
        return true;
    }

    /**
     * 处理顺序
     */
    int order();
}
