package cn.bixin.sona.server.im.listener;

import cn.bixin.sona.api.im.request.ChatroomMessageRequest;
import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.server.im.ack.AckMessageHandler;
import cn.bixin.sona.server.im.utils.ConvertUtils;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qinwei
 */
@Component
public class AckMessageListener {

    @Resource
    private AckMessageHandler ackMessageHandler;

    @KafkaListener(topics = "TOPIC_ROOM_ACK_MESSAGE", groupId = "ROOM_ACK_MESSAGE_GROUP_ID")
    public void listenAckChatRoomIM(ConsumerRecord<String, String> record) {
        ackMessageHandler.handleAckMessage(JSON.parseObject(record.value(), ChatroomMessageRequest.class));
    }

    @KafkaListener(topics = "TOPIC_CHATROOM_MESSAGE_ACK", groupId = "CHATROOM_MESSAGE_ACK_GROUP_ID")
    public void processNeedAckMessage(ConsumerRecord<String, String> record) {
        RoomMessageRequest request = JSON.parseObject(record.value(), RoomMessageRequest.class);
        ackMessageHandler.handleNeedAckMessage(ConvertUtils.conv2MessageWrap(request));
    }

}
