package cn.bixin.sona.server.im.controller;

import cn.bixin.sona.api.im.RouterRoomMessageService;
import cn.bixin.sona.api.im.request.GroupMessageRequest;
import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.server.im.handler.HandlerChain;
import cn.bixin.sona.server.im.manager.BoardCastMessageManager;
import cn.bixin.sona.server.im.manager.GroupMessageManager;
import cn.bixin.sona.server.im.manager.P2pMessageManager;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qinwei
 */
@DubboService
public class RouterRoomMessageController implements RouterRoomMessageService {

    @Resource
    private HandlerChain handlerChain;

    @Resource
    private GroupMessageManager groupMessageManager;

    @Resource
    private P2pMessageManager p2pMessageManager;

    @Resource
    private BoardCastMessageManager boardCastMessageManager;

    @Override
    public Response<String> sendChatRoomMessage(RoomMessageRequest request) {
        Response<Boolean> response = handlerChain.handle(request);
        return response.isSuccess() ? Response.success(request.getMessageId()) : Response.fail(response.getCode(), response.getMsg());
    }

    @Override
    public Response<String> sendGroupMessage(GroupMessageRequest request) {
        Response<Boolean> response = groupMessageManager.sentGroupMessage(request);
        return response.isSuccess() ? Response.success(request.getMessageId()) : Response.fail(response.getCode(), response.getMsg());
    }

    @Override
    public Response<Boolean> sendChatRoomMessages(List<RoomMessageRequest> list) {
        handlerChain.handle(list);
        return Response.success(true);
    }

    @Override
    public Response<Boolean> sendGroupMessages(List<GroupMessageRequest> list) {
        groupMessageManager.sentGroupMessages(list);
        return Response.success(true);
    }

    @Override
    public Response<String> sendChatRoomMessageToUids(RoomMessageRequest request, List<Long> uids) {
        Response<Boolean> response = p2pMessageManager.sendP2pMessage(request, uids);
        return response.isSuccess() ? Response.success(request.getMessageId()) : Response.fail(response.getCode(), response.getMsg());
    }

    @Override
    public Response<String> boardCastChatRoomMessage(RoomMessageRequest request) {
        boardCastMessageManager.boardCastChatRoomMessage(request);
        return Response.success(request.getMessageId());
    }

    @Override
    public Response<String> boardCastChatRoomMessage(RoomMessageRequest request, List<String> roomIds) {
        boardCastMessageManager.boardCastChatRoomMessage(request, roomIds);
        return Response.success(request.getMessageId());
    }

}
