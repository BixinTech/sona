package cn.bixin.sona.server.im.controller;

import cn.bixin.sona.api.im.RouteAckMessageService;
import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.api.socket.RoomMessageRemoteService;
import cn.bixin.sona.api.socket.request.ChatroomMsgRequest;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.server.im.utils.ConvertUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qinwei
 */
@Service
public class RouteAckMessageController implements RouteAckMessageService {

    @Resource
    private RoomMessageRemoteService roomMessageRemoteService;

    @Override
    public Response<Boolean> sendAckMessage(RoomMessageRequest request, List<Long> uids) {
        ChatroomMsgRequest chatroomMsg = ConvertUtils.convertChatroomMsgRequest(request);
        chatroomMsg.setAckUids(uids);
        return roomMessageRemoteService.batchSendChatroomMember(chatroomMsg, uids);
    }

}
