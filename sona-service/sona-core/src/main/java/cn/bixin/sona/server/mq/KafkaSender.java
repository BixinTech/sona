package cn.bixin.sona.server.mq;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author qinwei
 */
@Component
public class KafkaSender {

    private static final Logger log = LoggerFactory.getLogger(KafkaSender.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String key, Object object) {
        String json = JSON.toJSONString(object);
        kafkaTemplate.send(topic, key, json)
                .addCallback(o -> {
                }, throwable -> log.error("kafka消息发送失败: topic = {}, key = {} , json = {}", topic, key, json));
    }

    public void send(String topic, Object object) {
        send(topic, null, object);
    }

}
