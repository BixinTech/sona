package cn.bixin.sona.server.im.manager;

import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.server.im.service.MercurySendService;
import cn.bixin.sona.server.im.service.SaveMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author qinwei
 */
@Service
public class ChatRoomMessageManager {

    @Resource
    private MercurySendService mercurySendService;

    @Resource
    private SaveMessageService saveMessageService;

    public Response<Boolean> sentChatRoomMessage(RoomMessageRequest request) {
        Response<Boolean> response = mercurySendService.sendChatRoomMessage(request);
        if (response.isSuccess()) {
            saveMessageService.saveRoomMessage(request);
        }
        return response;
    }
}
