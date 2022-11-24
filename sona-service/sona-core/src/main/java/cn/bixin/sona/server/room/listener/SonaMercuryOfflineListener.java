package cn.bixin.sona.server.room.listener;


import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.server.mq.RocketSender;
import cn.bixin.sona.server.room.service.redis.ChatroomRedisRepo;
import cn.bixin.sona.session.api.UserSessionRemoteService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author qinwei
 */
@Component
@RocketMQMessageListener(topic = "TOPIC_SONA_MERCURY_OFFLINE", consumerGroup = "SONA_MERCURY_OFFLINE-SONA_GROUP")
public class SonaMercuryOfflineListener implements RocketMQListener<MessageExt> {

    private static final Logger log = LoggerFactory.getLogger(SonaMercuryOfflineListener.class);

    @Resource
    private ChatroomRedisRepo chatroomRedisRepo;

    @Resource
    private RocketSender rocketSender;

    @DubboReference
    private UserSessionRemoteService userSessionRemoteService;

    @Override
    public void onMessage(MessageExt messageExt) {
        Cat.logBatchEvent("RocketMQListener", getClass().getSimpleName(), 1, 0);
        if (ObjectUtils.isEmpty(messageExt.getBody())) {
            log.warn("SonaMercuryOfflineListener.onMessage, msg empty, msg={}", JSON.toJSONString(messageExt));
            return;
        }
        String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("SonaMercuryOfflineListener.onMessage， body={}, msgId={}", body, messageExt.getMsgId());
        JSONObject json = JSON.parseObject(body);
        Long roomId = json.getLong("room");
        Long uid = json.getLong("uid");
        Transaction t = Cat.newTransaction("MQListener.MercuryOffline", String.valueOf(roomId));
        t.addData("u", uid);
        try {
            Response<List<String>> response = userSessionRemoteService.getChatRoomOnlineState(uid);
            if (!response.isSuccess()) {
                log.error("SonaMercuryOfflineListener, getChatRoomOnlineState failed ! uid= {}", uid);
                rocketSender.sendDelay("TOPIC_SONA_MERCURY_OFFLINE", body, 1);
                return;
            }
            if (response.getResult().contains(String.valueOf(roomId))) {
                log.info("SonaMercuryOfflineListener, user reconnect. uid= {}", uid);
                return;
            }
            chatroomRedisRepo.leaveChatroom(roomId, uid);
        } catch (Exception e) {
            t.setStatus(e);
            log.error("SonaMercuryOfflineListener.onMessage fail, msg: {}", body, e);
        } finally {
            t.complete();
        }
    }

}
