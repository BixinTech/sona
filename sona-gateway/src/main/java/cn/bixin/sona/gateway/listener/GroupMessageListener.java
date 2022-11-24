package cn.bixin.sona.gateway.listener;

import cn.bixin.sona.gateway.cat.MonitorUtils;
import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.common.Header;
import cn.bixin.sona.gateway.common.HeaderEnum;
import cn.bixin.sona.gateway.util.AccessMessageUtils;
import cn.bixin.sona.gateway.util.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author qinwei
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "TOPIC_GROUP_MESSAGE_SEND", consumerGroup = "GROUP_MESSAGE-MERCURY_GROUP", messageModel = MessageModel.BROADCASTING)
public class GroupMessageListener implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt messageExt) {
        MonitorUtils.logBatchEvent("RocketMQListener", getClass().getSimpleName(), 1, 0);
        if (ObjectUtils.isEmpty(messageExt.getBody())) {
            log.warn("GroupMessageListener.onMessage, msg empty, msg={}", JSON.toJSONString(messageExt));
            return;
        }
        String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        try {
            JSONObject json = JSON.parseObject(body);
            String[] channels = json.getObject("channels", String[].class);
            if (channels == null || channels.length == 0) {
                return;
            }
            int cmd = json.getInteger(Constants.MQ_SEND_KEY_CMD);
            AccessMessage message = AccessMessageUtils.createRequest(cmd, json.getString(Constants.MQ_REPORT_KEY_DATA).getBytes(StandardCharsets.UTF_8));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.CHATROOM_MSG_KEY_ROOM, json.getString(Constants.MQ_REPORT_KEY_ROOM));
            message.addHeader(new Header(HeaderEnum.CHATROOM, jsonObject.toJSONString()));

            Arrays.stream(channels)
                    .map(NettyChannel::getChannel)
                    .filter(Objects::nonNull)
                    .forEach(channel -> channel.fastSend(message, true));
        } catch (Exception e) {
            log.error("GroupMessageListener.onMessage fail, msg: {}", body, e);
        }
    }

}
