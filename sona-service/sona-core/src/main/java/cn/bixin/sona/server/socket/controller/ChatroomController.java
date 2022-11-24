package cn.bixin.sona.server.socket.controller;


import cn.bixin.sona.api.room.SonaRoomQueryRemoteService;
import cn.bixin.sona.api.socket.RoomMessageRemoteService;
import cn.bixin.sona.api.socket.request.BatchChatroomMsgRequest;
import cn.bixin.sona.api.socket.request.ChatroomMsgRequest;
import cn.bixin.sona.api.socket.request.GroupMsgRequest;
import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.dto.RoomUserDTO;
import cn.bixin.sona.server.socket.service.ChatroomSendService;
import cn.bixin.sona.session.api.UserSessionRemoteService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author qinwei
 */
@Service
public class ChatroomController implements RoomMessageRemoteService {

    private UserSessionRemoteService userSessionRemoteService;

    @Resource
    private ChatroomSendService chatroomSendService;

    @Resource
    private SonaRoomQueryRemoteService sonaRoomQueryRemoteService;

    @Override
    public Response<Boolean> closeChatroom(String roomId) {
        return Response.success(chatroomSendService.asyncCloseRoom(roomId));
    }

    @Override
    public Response<Boolean> batchKickOutChatroom(ChatroomMsgRequest request, List<Long> toUids) {
        return Response.success(chatroomSendService.asyncKickOut(request, toUids.stream().map(Object::toString).collect(Collectors.toList())));
    }

    @Override
    public Response<Boolean> sendChatroom(ChatroomMsgRequest request) {
        return Response.success(chatroomSendService.asyncSendMessage(request));
    }

    @Override
    public Response<Boolean> batchSendChatroom(BatchChatroomMsgRequest request) {
        return Response.success(chatroomSendService.asyncBatchSendMessage(request));
    }

    @Override
    public Response<Boolean> globalSendChatroom(BatchChatroomMsgRequest request) {
        return Response.success(chatroomSendService.asyncGlobalSendMessage(request));
    }

    @Override
    public Response<Boolean> batchSendChatroomMember(ChatroomMsgRequest request, List<Long> toUids) {
        return Response.success(chatroomSendService.asyncSendMessage(request, toUids.stream().map(Objects::toString).collect(Collectors.toList())));
    }

    @Override
    public Response<Boolean> sendGroup(GroupMsgRequest request) {
        List<String> results = new ArrayList<>();
        String anchor = "0";
        int limit = 50;
        boolean end = false;
        while (!end) {
            Response<PageResult<RoomUserDTO>> response = sonaRoomQueryRemoteService.getRoomMemberList(Long.parseLong(request.getGroupId()), anchor, limit);
            boolean hasData = response.isSuccess() && response.getResult() != null && !CollectionUtils.isEmpty(response.getResult().getList());
            if (hasData) {
                Response<Map<Long, List<String>>> resp = userSessionRemoteService.getChatRoomOnlineStates(response.getResult().getList().stream().map(RoomUserDTO::getUid).collect(Collectors.toList()));
                if (resp.isSuccess()) {
                    Map<Long, List<String>> result = resp.getResult();
                    for (List<String> value : result.values()) {
                        results.addAll(value);
                    }
                }
            } else {
                break;
            }
            end = response.getResult().getEnd();
            anchor = response.getResult().getAnchor();
        }
        return Response.success(chatroomSendService.asyncSendGroupMessage(request, results));
    }

    @Override
    public Response<Boolean> sendMsgToGroupMembers(GroupMsgRequest request, List<Long> toUids) {
        Response<Map<Long, List<String>>> response = userSessionRemoteService.getChatRoomOnlineStates(toUids);
        if (!response.isSuccess()) {
            return Response.fail(response.getCode(), response.getMsg());
        }
        Map<Long, List<String>> result = response.getResult();
        List<String> channels = new ArrayList<>();
        for (List<String> value : result.values()) {
            channels.addAll(value);
        }
        return Response.success(chatroomSendService.asyncSendGroupMessage(request, channels));
    }

}

