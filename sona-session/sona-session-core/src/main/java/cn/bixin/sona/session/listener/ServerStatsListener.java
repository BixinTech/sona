package cn.bixin.sona.session.listener;


import cn.bixin.sona.session.utils.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dianping.cat.Cat;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * @author qinwei
 */
@Component
@RocketMQMessageListener(topic = "TOPIC_SERVER_STATS", consumerGroup = "SERVER_STATS-SESSION_GROUP", consumeMode = ConsumeMode.ORDERLY)
public class ServerStatsListener implements RocketMQListener<MessageExt> {

    private static final Logger log = LoggerFactory.getLogger(ServerStatsListener.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onMessage(MessageExt messageExt) {
        Cat.logBatchEvent("RocketMQListener", getClass().getSimpleName(), 1, 0);
        if (ObjectUtils.isEmpty(messageExt.getBody())) {
            log.warn("ServerStatsListener.onMessage, msg empty, msg={}", JSON.toJSONString(messageExt));
            return;
        }
        String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
        log.info("ServerStatsListener.onMessage， body={}, msgId={}", body, messageExt.getMsgId());
        try {
            JSONObject json = JSON.parseObject(body);
            String serverId = json.getString(Constants.MQ_REPORT_KEY_SERVER_ID);
            stringRedisTemplate.opsForHash().put("server_stat", serverId, body);
            stringRedisTemplate.expire("server_stat", Duration.ofDays(1));
        } catch (Exception e) {
            log.error("ServerStatsListener.onMessage fail, msg: {}", body, e);
        }
    }

}
