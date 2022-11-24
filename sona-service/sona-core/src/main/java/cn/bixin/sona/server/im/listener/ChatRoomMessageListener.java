package cn.bixin.sona.server.im.listener;

import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.server.im.message.MessageQueueManager;
import cn.bixin.sona.server.im.service.SendService;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qinwei
 */
@Component
public class ChatRoomMessageListener implements DisposableBean {

    @Resource
    private SendService sendService;

    @KafkaListener(topics = "TOPIC-ROOM-IM-MESSAGE", groupId = "ROOM-IM-MESSAGE_GROUP_ID")
    public void processChatroomMessage(ConsumerRecord<String, String> record) {
        sendService.sendMessage(JSON.parseObject(record.value(), RoomMessageRequest.class));
    }

    @Override
    public void destroy() throws Exception {
        MessageQueueManager.stop();
    }
}
