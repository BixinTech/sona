package cn.bixin.sona.session.listener;


import cn.bixin.sona.session.service.RoomSessionService;
import cn.bixin.sona.session.utils.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
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
@RocketMQMessageListener(topic = "TOPIC_SOCKET_ROOM_SESSION", consumerGroup = "SOCKET_ROOM_SESSION-SESSION_GROUP", consumeMode = ConsumeMode.ORDERLY)
public class RoomSessionListener implements RocketMQListener<MessageExt> {

    private static final Logger log = LoggerFactory.getLogger(RoomSessionListener.class);

    @Resource
    private RoomSessionService roomSessionService;

    @Override
    public void onMessage(MessageExt messageExt) {
        Cat.logBatchEvent("RocketMQListener", getClass().getSimpleName(), 1, 0);
        if (ObjectUtils.isEmpty(messageExt.getBody())) {
            log.warn("RoomSessionListener.onMessage, msg empty, msg={}", JSON.toJSONString(messageExt));
            return;
        }
        String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("RoomSessionListener.onMessage， body={}, msgId={}", body, messageExt.getMsgId());
        try {
            JSONObject json = JSON.parseObject(body);
            String type = json.getString(Constants.MQ_REPORT_KEY_TYPE);
            if (Constants.MQ_REPORT_VAL_TYPE_CONNECT.equals(type)) {
                processConnect(json);
            } else if (Constants.MQ_REPORT_VAL_TYPE_ROOM.equals(type)) {
                processRoomSession(json);
            } else {
                log.warn("RoomSessionListener.onMessage, UnknownType, body={}", body);
                Cat.logEvent(Constants.CHATROOM_SESSION_PROBLEM, "UnknownType");
            }
        } catch (Exception e) {
            log.error("RoomSessionListener.onMessage fail, msg: {}", body, e);
        }
    }

    private void processConnect(JSONObject json) {
        Integer session = json.getInteger(Constants.MQ_REPORT_KEY_SESSION);
        Transaction t = Cat.newTransaction("MQListener.Connect", String.valueOf(session));
        try {
            if (Constants.SESSION_ONLINE == session) {
                roomSessionService.processChannelActive(json);
            } else if (Constants.SESSION_OFFLINE == session) {
                roomSessionService.processChannelInactive(json);
            }
        } catch (Exception e) {
            t.setStatus(e);
            throw e;
        } finally {
            t.complete();
        }
    }

    private void processRoomSession(JSONObject json) {
        Integer cmd = json.getInteger(Constants.MQ_REPORT_KEY_CMD);
        Transaction t = Cat.newTransaction("MQListener.ChatRoomSession", String.valueOf(cmd));
        try {
            // 加入房间
            if (cmd == 10) {
                roomSessionService.processEnterRoom(json);
            } else if (cmd == 11 || cmd == 13) {
                // 离开房间、踢人、关闭房间
                roomSessionService.processLeaveRoom(json);
            }
        } catch (Exception e) {
            t.setStatus(e);
            throw e;
        } finally {
            t.complete();
        }
    }

}
