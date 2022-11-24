package cn.bixin.sona.server.im.utils;

import cn.bixin.sona.api.im.enums.PriorityEnum;
import cn.bixin.sona.api.im.request.ChatroomMessageRequest;
import cn.bixin.sona.api.im.request.GroupMessageRequest;
import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.api.socket.request.BatchChatroomMsgRequest;
import cn.bixin.sona.api.socket.request.ChatroomMsgRequest;
import cn.bixin.sona.api.socket.request.GroupMsgRequest;
import cn.bixin.sona.server.im.ack.data.MessageRequestWrap;
import cn.bixin.sona.server.im.dto.RoomMessageDTO;

import java.util.List;

/**
 * @author qinwei
 */
public class ConvertUtils {

    public static RoomMessageRequest convertChatroomMessageRequest(ChatroomMessageRequest request, String productCode) {
        RoomMessageRequest roomMessageRequest = new RoomMessageRequest();
        roomMessageRequest.setRoomId(request.getRoomId());
        roomMessageRequest.setPriority(PriorityEnum.getPriorityType(request.getPriority()));
        roomMessageRequest.setUid(request.getUid());
        roomMessageRequest.setMsgType(request.getMsgType());
        roomMessageRequest.setContent(request.getContent());
        roomMessageRequest.setSendTime(System.currentTimeMillis());
        roomMessageRequest.setMessageId(request.getMessageId());
        roomMessageRequest.setProductCode(productCode);
        roomMessageRequest.setNeedToSave(request.isNeedToSave());
        return roomMessageRequest;
    }

    public static GroupMessageRequest convertGroupMessageRequest(ChatroomMessageRequest request, String productCode) {
        GroupMessageRequest groupMessageRequest = new GroupMessageRequest();
        groupMessageRequest.setGroupId(request.getRoomId());
        groupMessageRequest.setUid(request.getUid());
        groupMessageRequest.setContent(request.getContent());
        groupMessageRequest.setSendTime(System.currentTimeMillis());
        groupMessageRequest.setMessageId(request.getMessageId());
        groupMessageRequest.setProductCode(productCode);
        groupMessageRequest.setNeedToSave(request.isNeedToSave());
        return groupMessageRequest;
    }

    public static RoomMessageDTO convertGroupSaveMessage(GroupMessageRequest request) {
        RoomMessageDTO roomMessage = new RoomMessageDTO();
        roomMessage.setRoomId(request.getGroupId());
        roomMessage.setContent(request.getContent());
        roomMessage.setUid(request.getUid());
        roomMessage.setSendTime(System.currentTimeMillis());
        roomMessage.setProductCode(request.getProductCode());
        roomMessage.setMessageId(request.getMessageId());
        return roomMessage;
    }

    public static RoomMessageDTO convertChatSaveMessage(RoomMessageRequest request) {
        RoomMessageDTO roomMessage = new RoomMessageDTO();
        roomMessage.setRoomId(request.getRoomId());
        roomMessage.setContent(request.getContent());
        roomMessage.setUid(request.getUid());
        roomMessage.setSendTime(System.currentTimeMillis());
        roomMessage.setMessageId(request.getMessageId());
        roomMessage.setProductCode(request.getProductCode());
        return roomMessage;
    }

    public static ChatroomMsgRequest convertChatroomMsgRequest(RoomMessageRequest request) {
        ChatroomMsgRequest chatroomMsg = new ChatroomMsgRequest();
        chatroomMsg.setRoomId(String.valueOf(request.getRoomId()));
        chatroomMsg.setContent(request.getContent());
        chatroomMsg.setSendTime(request.getSendTime() == null ? System.currentTimeMillis() : request.getSendTime());
        chatroomMsg.setHighPriority(PriorityEnum.HIGH == request.getPriority());
        chatroomMsg.setFromUid(String.valueOf(request.getUid()));
        chatroomMsg.setAckUids(request.getAckUids());
        chatroomMsg.setMessageId(request.getMessageId());
        chatroomMsg.setProductCode(request.getProductCode());
        return chatroomMsg;
    }

    public static GroupMsgRequest convertGroupMsgDTO(GroupMessageRequest request) {
        GroupMsgRequest groupMsg = new GroupMsgRequest();
        groupMsg.setGroupId(String.valueOf(request.getGroupId()));
        groupMsg.setContent(request.getContent());
        groupMsg.setSendTime(request.getSendTime());
        groupMsg.setMessageId(request.getMessageId());
        return groupMsg;
    }

    public static BatchChatroomMsgRequest convertBatchChatroomMsg(RoomMessageRequest request) {
        BatchChatroomMsgRequest msgRequest = new BatchChatroomMsgRequest();
        msgRequest.setContent(request.getContent());
        msgRequest.setSendTime(request.getSendTime());
        msgRequest.setAckUids(request.getAckUids());
        msgRequest.setMessageId(request.getMessageId());
        msgRequest.setBusinessType(request.getProductCode());
        return msgRequest;
    }

    public static BatchChatroomMsgRequest convertBatchChatroomMsg(RoomMessageRequest request, List<String> roomIds) {
        BatchChatroomMsgRequest msgRequest = new BatchChatroomMsgRequest();
        msgRequest.setContent(request.getContent());
        msgRequest.setSendTime(request.getSendTime());
        msgRequest.setRoomIds(roomIds);
        msgRequest.setAckUids(request.getAckUids());
        msgRequest.setMessageId(request.getMessageId());
        msgRequest.setBusinessType(request.getProductCode());
        return msgRequest;
    }

    public static MessageRequestWrap conv2MessageWrap(RoomMessageRequest request) {
        MessageRequestWrap wrap = new MessageRequestWrap();
        wrap.setRequest(request);
        return wrap;
    }
}
