package cn.bixin.sona.server.im.manager;

import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.server.im.service.MercurySendService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qinwei
 */
@Service
public class BoardCastMessageManager {

    @Resource
    private MercurySendService mercurySendService;

    public Response<Boolean> boardCastChatRoomMessage(RoomMessageRequest request) {
        return mercurySendService.globalSendChatroom(request);
    }

    public Response<Boolean> boardCastChatRoomMessage(RoomMessageRequest request, List<String> roomIds) {
        return mercurySendService.batchSendChatroom(request, roomIds);
    }
}
