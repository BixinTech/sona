package cn.bixin.sona.server.im.service;

import cn.bixin.sona.api.im.request.GroupMessageRequest;
import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.api.socket.RoomMessageRemoteService;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.server.im.utils.ConvertUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qinwei
 */
@Service
public class MercurySendService {

    @Resource
    private RoomMessageRemoteService roomMessageRemoteService;

    public Response<Boolean> sendGroupMessage(GroupMessageRequest request) {
        return roomMessageRemoteService.sendGroup(ConvertUtils.convertGroupMsgDTO(request));
    }

    public Response<Boolean> sendP2pMessage(RoomMessageRequest request, List<Long> uids) {
        return roomMessageRemoteService.batchSendChatroomMember(ConvertUtils.convertChatroomMsgRequest(request), uids);
    }

    public Response<Boolean> sendChatRoomMessage(RoomMessageRequest request) {
        return roomMessageRemoteService.sendChatroom(ConvertUtils.convertChatroomMsgRequest(request));
    }

    public Response<Boolean> globalSendChatroom(RoomMessageRequest request) {
        return roomMessageRemoteService.globalSendChatroom(ConvertUtils.convertBatchChatroomMsg(request));
    }

    public Response<Boolean> batchSendChatroom(RoomMessageRequest request, List<String> roomIds) {
        return roomMessageRemoteService.batchSendChatroom(ConvertUtils.convertBatchChatroomMsg(request, roomIds));
    }
}
