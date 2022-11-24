package cn.bixin.sona.server.im.handler;

import cn.bixin.sona.api.im.enums.PriorityEnum;
import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.common.dto.Response;
import org.springframework.stereotype.Service;

/**
 * @author qinwei
 */
@Service
public class MediumLevelHandler extends AbstractChatRoomHandler {

    @Override
    public Response<Boolean> doHandle(RoomMessageRequest request) {
        // 中等级消息直接发kafka
        return sendKafka(request);
    }

    @Override
    public boolean support(RoomMessageRequest request) {
        return PriorityEnum.MEDIUM == request.getPriority();
    }

    @Override
    public int order() {
        return 3;
    }
}
