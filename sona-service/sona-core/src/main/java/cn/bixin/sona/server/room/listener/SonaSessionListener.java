package cn.bixin.sona.server.room.listener;


import cn.bixin.sona.dto.RoomDTO;
import cn.bixin.sona.server.mq.RocketSender;
import cn.bixin.sona.server.room.domain.db.ProductConfig;
import cn.bixin.sona.server.room.domain.enums.IMModuleEnum;
import cn.bixin.sona.server.room.service.ProductConfigService;
import cn.bixin.sona.server.room.service.RoomService;
import cn.bixin.sona.server.room.service.redis.ChatroomRedisRepo;
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
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author qinwei
 */
@Component
@RocketMQMessageListener(topic = "TOPIC_CHATROOM_SESSION_SONA", consumerGroup = "CHATROOM_SESSION-SONA_GROUP", consumeMode = ConsumeMode.ORDERLY)
public class SonaSessionListener implements RocketMQListener<MessageExt> {

    private static final Logger log = LoggerFactory.getLogger(SonaSessionListener.class);

    @Resource
    private ProductConfigService productConfigService;

    @Resource
    private RoomService roomService;

    @Resource
    private ChatroomRedisRepo chatroomRedisRepo;

    @Resource
    private RocketSender rocketSender;

    @Override
    public void onMessage(MessageExt messageExt) {
        Cat.logBatchEvent("RocketMQListener", getClass().getSimpleName(), 1, 0);
        if (ObjectUtils.isEmpty(messageExt.getBody())) {
            log.warn("SonaSessionListener.onMessage, msg empty, msg={}", JSON.toJSONString(messageExt));
            return;
        }

        String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("SonaSessionListener.onMessage， body={}, msgId={}", body, messageExt.getMsgId());
        JSONObject json = JSON.parseObject(body);
        Long roomId = json.getLong("room");
        Transaction t = Cat.newTransaction("MQListener.SonaSession", String.valueOf(roomId));
        try {
            RoomDTO roomDTO = roomService.getRoomByRoomId(roomId);
            if (roomDTO == null) {
                log.error("SonaSessionListener, the room not exist, roomId={}", roomId);
                return;
            }
            ProductConfig productConfig = productConfigService.getConfigInfoByCode(roomDTO.getProductCode());
            if (!IMModuleEnum.CHATROOM.name().equals(productConfig.getImModule())) {
                log.warn("SonaSessionListener roomId is belong group. roomId= {}", roomId);
                return;
            }
            String uid = json.getString("uid");
            if (json.getIntValue("cmd") == 10) {
                handEnter(roomId, uid);
            } else {
                // 业务正常离开,直接退出房间，否则延迟检测
                if (json.getIntValue("reason") == 1) {
                    chatroomRedisRepo.leaveChatroom(roomId, Long.parseLong(uid));
                } else {
                    rocketSender.sendDelay("TOPIC_SONA_MERCURY_OFFLINE", body, 2);
                }
            }
        } catch (Exception e) {
            t.setStatus(e);
            log.error("SonaSessionListener.onMessage fail, msg: {}", body, e);
        } finally {
            t.complete();
        }
    }

    private void handEnter(Long roomId, String uid) {
        //踢出房间校验
        if (chatroomRedisRepo.hasKickOut(roomId, uid)) {
            log.warn("SonaSessionListener, the user has been kicked out from room , uid={}, roomId={}", uid, roomId);
            return;
        }
        //是否已经存在
        if (chatroomRedisRepo.userExistInOnlineList(roomId, uid)) {
            log.info("SonaSessionListener, the user has been enter room uid={}, roomId={}", uid, roomId);
            return;
        }
        int score = StringUtils.hasText(chatroomRedisRepo.getChatroomVipUser(roomId, uid)) ? 100 : 1;
        chatroomRedisRepo.enterChatroom(roomId, uid, score);
    }

}
