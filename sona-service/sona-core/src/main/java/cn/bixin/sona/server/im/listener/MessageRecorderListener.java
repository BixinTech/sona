package cn.bixin.sona.server.im.listener;

import cn.bixin.sona.server.im.dto.RoomMessageDTO;
import cn.bixin.sona.server.im.service.MessageRecorderService;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MessageRecorderListener {

    private static final Logger log = LoggerFactory.getLogger(MessageRecorderListener.class);

    @Resource
    MessageRecorderService messageRecorderService;

    @KafkaListener(topics = "TOPIC-ROOM-MESSAGE-RECORDER", groupId = "CHATROOM_MESSAGE_ACK_GROUP_ID")
    public void recordRoomMessage(ConsumerRecord<String, String> record) {
        try {
            RoomMessageDTO request = JSON.parseObject(record.value(), RoomMessageDTO.class);
            messageRecorderService.saveRoomMessage(request);
        } catch (Exception e){
            log.error("recordRoomMessage exception, record:{}", record.value(), e);
        }
    }
}
