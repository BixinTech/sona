package cn.bixin.sona.server.socket.service;

import cn.bixin.sona.api.socket.request.BatchChatroomMsgRequest;
import cn.bixin.sona.api.socket.request.ChatroomMsgRequest;
import cn.bixin.sona.api.socket.request.GroupMsgRequest;
import cn.bixin.sona.server.im.utils.MessageLog;
import cn.bixin.sona.server.mq.RocketSender;
import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qinwei
 */
@Service
public class ChatroomSendService {

    @Resource
    private MessageLog messageLog;

    @Resource
    private RocketSender rocketSender;

    private static final String MQ_KEY_CMD = "cmd";
    private static final String MQ_KEY_ROOM = "room";
    private static final String MQ_KEY_DATA = "data";
    private static final String MQ_KEY_SEND_TIME = "sendTime";
    private static final String MQ_KEY_MEMBER_LIST = "members";
    private static final String MQ_KEY_PRIORITY = "highPriority";
    private static final String MQ_KEY_ACK_UIDS = "ackUids";
    private static final String MQ_KEY_MESSAGE_ID = "messageId";
    private static final String MQ_KEY_SIGNAL = "signal";

    public boolean asyncSendMessage(ChatroomMsgRequest request) {
        JSONObject param = new JSONObject();
        if (request.getSendTime() > 0) {
            param.put(MQ_KEY_SEND_TIME, request.getSendTime());
        }
        if (!CollectionUtils.isEmpty(request.getAckUids())) {
            param.put(MQ_KEY_ACK_UIDS, request.getAckUids().stream().map(String::valueOf).collect(Collectors.toList()));
        }
        param.put(MQ_KEY_ROOM, request.getRoomId());
        param.put(MQ_KEY_DATA, request.getContent());
        param.put(MQ_KEY_CMD, 12);
        param.put(MQ_KEY_PRIORITY, request.isHighPriority());
        param.put(MQ_KEY_MESSAGE_ID, request.getMessageId());
        return sendChatroom(param);
    }

    public boolean asyncBatchSendMessage(BatchChatroomMsgRequest request) {
        JSONObject param = new JSONObject();
        if (request.getSendTime() > 0) {
            param.put(MQ_KEY_SEND_TIME, request.getSendTime());
        }
        if (!CollectionUtils.isEmpty(request.getAckUids())) {
            param.put(MQ_KEY_ACK_UIDS, request.getAckUids().stream().map(String::valueOf).collect(Collectors.toList()));
        }
        param.put("rooms", request.getRoomIds());
        param.put(MQ_KEY_DATA, request.getContent());
        param.put(MQ_KEY_CMD, 12);
        param.put(MQ_KEY_MESSAGE_ID, request.getMessageId());
        return sendChatroom(param);
    }

    public boolean asyncGlobalSendMessage(BatchChatroomMsgRequest request) {
        JSONObject param = new JSONObject();
        if (request.getSendTime() > 0) {
            param.put(MQ_KEY_SEND_TIME, request.getSendTime());
        }
        if (!CollectionUtils.isEmpty(request.getAckUids())) {
            param.put(MQ_KEY_ACK_UIDS, request.getAckUids().stream().map(String::valueOf).collect(Collectors.toList()));
        }
        param.put("global", true);
        param.put(MQ_KEY_DATA, request.getContent());
        param.put(MQ_KEY_CMD, 12);
        param.put(MQ_KEY_MESSAGE_ID, request.getMessageId());
        return sendChatroom(param);
    }

    public boolean asyncSendMessage(ChatroomMsgRequest request, List<String> toUids) {
        JSONObject param = new JSONObject();
        if (request.getSendTime() > 0) {
            param.put(MQ_KEY_SEND_TIME, request.getSendTime());
        }
        if (!CollectionUtils.isEmpty(request.getAckUids())) {
            param.put(MQ_KEY_ACK_UIDS, request.getAckUids().stream().map(String::valueOf).collect(Collectors.toList()));
        }
        param.put(MQ_KEY_ROOM, request.getRoomId());
        param.put(MQ_KEY_DATA, request.getContent());
        param.put(MQ_KEY_MEMBER_LIST, toUids);
        param.put(MQ_KEY_CMD, 12);
        param.put(MQ_KEY_PRIORITY, request.isHighPriority());
        param.put(MQ_KEY_MESSAGE_ID, request.getMessageId());
        return sendChatroom(param);
    }

    public boolean asyncCloseRoom(String roomId) {
        JSONObject param = new JSONObject();
        param.put(MQ_KEY_ROOM, roomId);
        param.put(MQ_KEY_SIGNAL, 2);
        param.put(MQ_KEY_DATA, "");
        param.put(MQ_KEY_CMD, 13);
        return sendChatroom(param);
    }

    public boolean asyncKickOut(ChatroomMsgRequest request, List<String> members) {
        JSONObject param = new JSONObject();
        if (!CollectionUtils.isEmpty(request.getAckUids())) {
            param.put(MQ_KEY_ACK_UIDS, request.getAckUids().stream().map(String::valueOf).collect(Collectors.toList()));
        }
        param.put(MQ_KEY_ROOM, request.getRoomId());
        param.put(MQ_KEY_MEMBER_LIST, members);
        param.put(MQ_KEY_SIGNAL, 1);
        param.put(MQ_KEY_DATA, request.getContent());
        param.put(MQ_KEY_MESSAGE_ID, request.getMessageId());
        param.put(MQ_KEY_CMD, 13);
        return sendChatroom(param);
    }

    private boolean sendChatroom(JSONObject jsonObject) {
        String data = jsonObject.getString(MQ_KEY_DATA);
        rocketSender.asyncSend("TOPIC_CHATROOM_MESSAGE_SEND", jsonObject.toJSONString(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                if (StringUtils.hasText(data)) {
                    messageLog.saveMessageLog(jsonObject.getString(MQ_KEY_MESSAGE_ID), sendResult.getMsgId());
                }
            }

            @Override
            public void onException(Throwable e) {
                if (StringUtils.hasText(data)) {
                    messageLog.saveMessageLog(jsonObject.getString(MQ_KEY_MESSAGE_ID), e.getMessage());
                }
            }
        });
        return true;
    }

    public boolean asyncSendGroupMessage(GroupMsgRequest request, List<String> channels) {
        JSONObject param = new JSONObject();
        if (request.getSendTime() > 0) {
            param.put(MQ_KEY_SEND_TIME, request.getSendTime());
        }
        param.put(MQ_KEY_ROOM, request.getGroupId());
        param.put(MQ_KEY_DATA, request.getContent());
        param.put("channels", channels);
        param.put(MQ_KEY_CMD, 14);
        param.put(MQ_KEY_MESSAGE_ID, request.getMessageId());
        rocketSender.asyncSend("TOPIC_GROUP_MESSAGE_SEND", param.toJSONString(), null);
        return true;
    }

}
