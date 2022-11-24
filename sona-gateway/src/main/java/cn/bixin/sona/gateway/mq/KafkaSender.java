package cn.bixin.sona.gateway.mq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author qinwei
 */
@Component
@Slf4j
public class KafkaSender {

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
