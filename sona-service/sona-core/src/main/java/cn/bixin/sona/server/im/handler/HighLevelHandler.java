package cn.bixin.sona.server.im.handler;

import cn.bixin.sona.api.im.enums.PriorityEnum;
import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.server.im.flow.FlowStrategy;
import org.springframework.stereotype.Service;

/**
 * @author qinwei
 */
@Service
public class HighLevelHandler extends AbstractChatRoomHandler {

    @Override
    public Response<Boolean> doHandle(RoomMessageRequest request) {
        // 高等级消息有频控，通过直接发送，否则返回失败
        if (FlowStrategy.PASS == getStrategy(request)) {
            return chatRoomMessageManager.sentChatRoomMessage(request);
        }
        return Response.fail("9020", "flow control");
    }

    @Override
    public boolean support(RoomMessageRequest request) {
        return PriorityEnum.HIGH == request.getPriority();
    }

    @Override
    public int order() {
        return 1;
    }
}
