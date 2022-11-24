package cn.bixin.sona.server.im.listener;

import cn.bixin.sona.api.im.MessageCallbackService;
import cn.bixin.sona.api.im.enums.MsgFormatEnum;
import cn.bixin.sona.api.im.request.ChatroomMessageRequest;
import cn.bixin.sona.server.im.ack.AckMessageHandler;
import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;


/**
 * @author qinwei
 */
@Component
@RocketMQMessageListener(topic = "TOPIC_ROOM_MESSAGE", consumerGroup = "ROOM_MESSAGE-SONA_GROUP")
public class MessageListener implements RocketMQListener<MessageExt> {

    private static final Logger log = LoggerFactory.getLogger(MessageListener.class);

    @Resource
    private AckMessageHandler ackMessageHandler;

    @Resource
    private MessageCallbackService messageCallbackService;

    @Override
    public void onMessage(MessageExt messageExt) {
        if (ObjectUtils.isEmpty(messageExt.getBody())) {
            log.error("MessageListener.onMessage, msg empty, msg: {}", JSON.toJSONString(messageExt));
            return;
        }
        String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        try {
            String data = JSON.parseObject(message).getString("data");
            ChatroomMessageRequest request = JSON.parseObject(data, ChatroomMessageRequest.class);
            if (request == null) {
                log.error("MessageListener.onMessage, request empty, msg: {}", JSON.toJSONString(messageExt));
                return;
            }
            if (MsgFormatEnum.ACK.getCode() == request.getMsgFormat()) {
                ackMessageHandler.handleAckMessage(request);
            } else {
                messageCallbackService.sendChatroomMessage(request);
            }
        } catch (Exception e) {
            log.error("MessageListener.onMessage fail, msg: {}", message, e);
        }
    }

}
