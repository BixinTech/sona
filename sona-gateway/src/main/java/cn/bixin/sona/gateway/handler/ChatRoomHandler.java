package cn.bixin.sona.gateway.handler;

import cn.bixin.sona.api.im.MessageCallbackService;
import cn.bixin.sona.api.im.enums.MsgFormatEnum;
import cn.bixin.sona.api.im.request.ChatroomMessageRequest;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.gateway.cat.MonitorUtils;
import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.channel.RoomChannelManager;
import cn.bixin.sona.gateway.channel.support.ChannelAttrs;
import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.common.CommandEnum;
import cn.bixin.sona.gateway.common.HeaderEnum;
import cn.bixin.sona.gateway.config.ApolloConfiguration;
import cn.bixin.sona.gateway.exception.RemoteException;
import cn.bixin.sona.gateway.msg.AccessResponse;
import cn.bixin.sona.gateway.service.SocketNotifyService;
import cn.bixin.sona.gateway.util.AccessMessageUtils;
import cn.bixin.sona.gateway.util.Constants;
import cn.bixin.sona.gateway.util.EventRecordLog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static cn.bixin.sona.gateway.msg.AccessResponse.*;

/**
 * @author qinwei
 * <p>
 * 房间处理
 */
@Service("chatRoom")
public class ChatRoomHandler extends AbstractHandler {

    public static final String CHATROOM_EVENT = "ChatRoom";

    @Resource
    private ApolloConfiguration apolloConfiguration;

    @Resource
    private SocketNotifyService socketNotifyService;

    @DubboReference
    private MessageCallbackService messageCallbackService;

    @Override
    protected Object doHandle(NettyChannel channel, AccessMessage message) throws RemoteException {
        ChannelAttrs attrs = channel.getAttrs();
        Set<String> lastRooms = attrs.getRooms();
        String header = AccessMessageUtils.extractHeaderData(message, HeaderEnum.CHATROOM);
        JSONObject headerObj = JSON.parseObject(header);
        if (headerObj == null) {
            EventRecordLog.logEvent(channel, CHATROOM_EVENT, message, "Room header not found : " + lastRooms);
            MonitorUtils.logEvent(MonitorUtils.CHATROOM_PROBLEM, "RoomHeaderNotFound");
            return ROOM_HEADER_NOT_FOUND;
        }

        String room = headerObj.getString(Constants.CHATROOM_MSG_KEY_ROOM);
        if (!StringUtils.hasText(room)) {
            EventRecordLog.logEvent(channel, CHATROOM_EVENT, message, "Invalid room name");
            MonitorUtils.logEvent(MonitorUtils.CHATROOM_PROBLEM, "InvalidRoomName");
            return INVALID_ROOM_NAME;
        }

        int cmd = message.getCmd();
        if (CommandEnum.CHATROOM_SEND.getCommand() == cmd) {
            return handMessageSend(channel, message, room);
        } else if (CommandEnum.CHATROOM_JOIN.getCommand() == cmd) {
            return handleJoin(channel, message, headerObj, room);
        } else if (CommandEnum.CHATROOM_LEAVE.getCommand() == cmd) {
            return handleLeave(channel, message, room);
        } else {
            EventRecordLog.logEvent(channel, CHATROOM_EVENT, message, "Unknown cmd");
            MonitorUtils.logEvent(MonitorUtils.CHATROOM_PROBLEM, "UnknownCmd");
            return null;
        }
    }

    private AccessResponse handMessageSend(NettyChannel channel, AccessMessage message, String room) {
        ChannelAttrs attrs = channel.getAttrs();
        Set<String> lastRooms = attrs.getRooms();
        if (lastRooms == null || !lastRooms.contains(room)) {
            EventRecordLog.logEvent(channel, CHATROOM_EVENT, message, "Not in room when send : " + lastRooms);
            MonitorUtils.logEvent(MonitorUtils.CHATROOM_PROBLEM, "NotInRoomWhenSend");
            return NOT_IN_ROOM_WHEN_SEND;
        }
        String uid = attrs.getUid();
        if (!StringUtils.hasText(uid)) {
            EventRecordLog.logEvent(channel, CHATROOM_EVENT, message, "Empty uid");
            MonitorUtils.logEvent(MonitorUtils.CHATROOM_PROBLEM, "EmptyUid");
            return EMPTY_UID;
        }
        if (attrs.isVisitor()) {
            EventRecordLog.logEvent(channel, CHATROOM_EVENT, message, "Visitor send");
            MonitorUtils.logEvent(MonitorUtils.CHATROOM_PROBLEM, "VisitorSend");
            return VISITOR_SEND;
        }
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        if (apolloConfiguration.isRoomMessageAsync()) {
            socketNotifyService.notifyChatRoomMessage(channel, uid, room, body);
            return SUCCESS;
        }

        ChatroomMessageRequest request = JSON.parseObject(body, ChatroomMessageRequest.class);
        if (MsgFormatEnum.ACK.getCode() == request.getMsgFormat()) {
            socketNotifyService.notifyChatRoomMessage(channel, uid, room, body);
            return SUCCESS;
        }
        Response<Boolean> response = messageCallbackService.sendChatroomMessage(request);
        if (response.isSuccess() && response.getResult()) {
            return SUCCESS;
        }
        return new AccessResponse(Integer.parseInt(response.getCode()), response.getMsg());
    }

    private AccessResponse handleJoin(NettyChannel channel, AccessMessage message, JSONObject headerObj, String room) {
        ChannelAttrs attrs = channel.getAttrs();
        Set<String> lastRooms = attrs.getRooms();
        String uid = headerObj.getString(Constants.CHATROOM_MSG_KEY_UID);
        if (!StringUtils.hasText(uid)) {
            EventRecordLog.logEvent(channel, CHATROOM_EVENT, message, "Invalid uid");
            MonitorUtils.logEvent(MonitorUtils.CHATROOM_PROBLEM, "InvalidUid");
            return INVALID_UID;
        }
        if (!uid.equals(attrs.getUid()) && !CollectionUtils.isEmpty(lastRooms)) {
            EventRecordLog.logEvent(channel, CHATROOM_EVENT, message, "uid changed when in room : " + lastRooms);
            MonitorUtils.logEvent(MonitorUtils.CHATROOM_PROBLEM, "UidChangedWhenInRoom");
            return new AccessResponse(8010, "you should leave room [" + lastRooms + "] first before changing to another user!");
        }
        String identity = headerObj.getString(Constants.CHATROOM_MSG_KEY_IDENTITY);
        attrs.setVisitor("0".equals(identity));
        attrs.setUid(uid);

        RoomChannelManager.MANAGER_FOR_CHATROOM.addChannel(room, channel, uid);
        socketNotifyService.notifyChatRoomSession(channel, message.getCmd(), room, uid);
        EventRecordLog.logEvent(channel, CHATROOM_EVENT, message, room);
        return SUCCESS;
    }

    private AccessResponse handleLeave(NettyChannel channel, AccessMessage message, String room) {
        ChannelAttrs attrs = channel.getAttrs();
        Set<String> lastRooms = attrs.getRooms();
        if (lastRooms == null || !lastRooms.contains(room)) {
            EventRecordLog.logEvent(channel, CHATROOM_EVENT, message, "Not in room when leave : " + lastRooms);
            MonitorUtils.logEvent(MonitorUtils.CHATROOM_PROBLEM, "NotInRoomWhenLeave");
        }
        RoomChannelManager.MANAGER_FOR_CHATROOM.removeChannel(room, channel);
        String uid = attrs.getUid();
        if (!StringUtils.hasText(uid)) {
            EventRecordLog.logEvent(channel, CHATROOM_EVENT, message, "Empty uid");
            MonitorUtils.logEvent(MonitorUtils.CHATROOM_PROBLEM, "EmptyUid");
            return EMPTY_UID;
        }
        socketNotifyService.notifyChatRoomSession(channel, message.getCmd(), room, attrs.getUid());
        EventRecordLog.logEvent(channel, CHATROOM_EVENT, message, room);
        return SUCCESS;
    }

}
