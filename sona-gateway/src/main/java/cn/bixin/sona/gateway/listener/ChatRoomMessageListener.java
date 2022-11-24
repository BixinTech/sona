package cn.bixin.sona.gateway.listener;

import cn.bixin.sona.gateway.cat.MonitorUtils;
import cn.bixin.sona.gateway.channel.RoomChannelManager;
import cn.bixin.sona.gateway.channel.support.RoomMessageTask;
import cn.bixin.sona.gateway.concurrent.FastThreadPool;
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
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author qinwei
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "TOPIC_CHATROOM_MESSAGE_SEND", consumerGroup = "CHATROOM_MESSAGE-MERCURY_GROUP", messageModel = MessageModel.BROADCASTING)
public class ChatRoomMessageListener implements RocketMQListener<MessageExt> {

    private final FastThreadPool threadPool = new FastThreadPool("chatroom", 18);

    @Override
    public void onMessage(MessageExt messageExt) {
        MonitorUtils.logBatchEvent("RocketMQListener", getClass().getSimpleName(), 1, 0);
        if (ObjectUtils.isEmpty(messageExt.getBody())) {
            log.warn("ChatRoomMessageListener.onMessage, msg empty, msg={}", JSON.toJSONString(messageExt));
            return;
        }
        String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        try {
            JSONObject json = JSON.parseObject(body);
            boolean global = Boolean.TRUE.equals(json.getBoolean(Constants.MQ_REPORT_KEY_GLOBAL));
            String roomVal = json.getString(Constants.MQ_REPORT_KEY_ROOM);
            String[] roomList = StringUtils.hasText(roomVal) ? new String[]{roomVal} : json.getObject(Constants.MQ_REPORT_KEY_ROOMS, String[].class);
            if (!global && ObjectUtils.isEmpty(roomList)) {
                log.warn("ChatRoomMessageListener.onMessage fail, msgId={}, body={}", messageExt.getMsgId(), body);
                MonitorUtils.logEvent(MonitorUtils.MQ_LISTENER_PROBLEM, "RequiredParamNotFound");
                return;
            }
            asyncHandle(json, global, roomList);
        } catch (Exception e) {
            log.error("ChatRoomMessageListener.onMessage fail, msg: {}", body, e);
        }
    }

    public void asyncHandle(JSONObject json, boolean global, String[] roomList) {
        RoomMessageTask[] tasks;
        if (global) {
            tasks = RoomChannelManager.MANAGER_FOR_CHATROOM.getAllRoomInfos().stream().map(roomInfo -> RoomMessageTask.newTask(json, roomInfo)).toArray(RoomMessageTask[]::new);
        } else {
            tasks = Arrays.stream(roomList).map(RoomChannelManager.MANAGER_FOR_CHATROOM::getRoomInfo).filter(Objects::nonNull).map(roomInfo -> RoomMessageTask.newTask(json, roomInfo)).toArray(RoomMessageTask[]::new);
        }
        threadPool.submit(tasks, 0, tasks.length);
    }

}
