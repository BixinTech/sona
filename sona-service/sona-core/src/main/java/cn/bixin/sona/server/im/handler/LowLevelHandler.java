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
public class LowLevelHandler extends AbstractChatRoomHandler {

    @Override
    public Response<Boolean> doHandle(RoomMessageRequest request) {
        // 低等级消息有频控，通过发kafka ，否则返回失败
        // 低等级消息比较多，此时不消耗令牌，只是为了提前拦截
        if (FlowStrategy.PASS == getStrategy(request)) {
            return sendKafka(request);
        }
        return Response.fail("9020", "flow control");
    }

    @Override
    public boolean support(RoomMessageRequest request) {
        return PriorityEnum.LOW == request.getPriority();
    }

    @Override
    public int order() {
        return 4;
    }
}
