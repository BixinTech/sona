package cn.bixin.sona.server.im.controller;

import cn.bixin.sona.api.im.MessageCallbackService;
import cn.bixin.sona.api.im.RouterRoomMessageService;
import cn.bixin.sona.api.im.callback.MessageCallbackRemoteService;
import cn.bixin.sona.api.im.request.ChatroomMessageRequest;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.dto.RoomDTO;
import cn.bixin.sona.server.exception.ExceptionCode;
import cn.bixin.sona.server.im.callback.ServiceGenerator;
import cn.bixin.sona.server.im.utils.ConvertUtils;
import cn.bixin.sona.server.room.domain.enums.IMModuleEnum;
import cn.bixin.sona.server.room.service.ProductConfigService;
import cn.bixin.sona.server.room.service.RoomService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author qinwei
 */
@DubboService
public class MessageCallbackController implements MessageCallbackService {

    @Resource
    private ServiceGenerator serviceGenerator;

    @Resource
    private ProductConfigService productConfigService;

    @Resource
    private RouterRoomMessageService routerRoomMessageService;

    @Resource
    private RoomService roomService;

    @Override
    public Response<Boolean> sendChatroomMessage(ChatroomMessageRequest request) {
        //获取房间信息
        RoomDTO roomDTO = roomService.getRoomByRoomId(request.getRoomId());
        if (roomDTO == null) {
            return Response.fail(ExceptionCode.CHATROOM_NOT_EXISTS);
        }
        MessageCallbackRemoteService service = serviceGenerator.getService(MessageCallbackRemoteService.class, roomDTO.getProductCode());
        Response<ChatroomMessageRequest> response = service.checkMessageCallback(request);
        if (!response.isSuccess()) {
            return Response.fail(response.getCode(), response.getMsg(), response.getExt());
        }
        //发送房间消息
        Response<String> sendResponse = sendMessage(response.getResult(), roomDTO.getProductCode());
        return sendResponse.isSuccess() ? Response.success(true) : Response.fail(sendResponse.getCode(), sendResponse.getMsg());
    }

    public Response<String> sendMessage(ChatroomMessageRequest request, String productCode) {
        // 发送聊天室消息
        if (IMModuleEnum.CHATROOM.name().equals(productConfigService.getConfigInfoByCode(productCode).getImModule())) {
            return routerRoomMessageService.sendChatRoomMessage(ConvertUtils.convertChatroomMessageRequest(request, productCode));
        }
        //发送群组消息
        return routerRoomMessageService.sendGroupMessage(ConvertUtils.convertGroupMessageRequest(request, productCode));
    }

}
