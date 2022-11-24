package cn.bixin.sona.server.mq;

import com.alibaba.fastjson.JSON;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * @author qinwei
 */
@Service
public class RocketSender {

    private static final Logger log = LoggerFactory.getLogger(RocketSender.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendDelay(String topic, String content, int delayLevel) {
        Transaction t = Cat.newTransaction("RocketMQ", topic);
        try {
            SendResult result = rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(content).build(), 1000, delayLevel);
            if (result == null || result.getSendStatus() != SendStatus.SEND_OK) {
                log.warn("sendDelay error. {}", JSON.toJSONString(result));
            }
        } catch (Exception e) {
            t.setStatus(e);
        } finally {
            t.complete();
        }
    }

    public SendResult syncSend(String topic, String content) {
        Transaction t = Cat.newTransaction("RocketMQ", topic);
        try {
            MessageBuilder<?> builder = MessageBuilder.withPayload(content);
            return rocketMQTemplate.syncSend(topic, builder.build());
        } catch (Exception e) {
            t.setStatus(e);
        } finally {
            t.complete();
        }
        return null;
    }

    public void asyncSend(String topic, String content, SendCallback callback) {
        Transaction t = Cat.newTransaction("RocketMQ", topic);
        try {
            MessageBuilder<?> builder = MessageBuilder.withPayload(content);
            rocketMQTemplate.asyncSend(topic, builder.build(), new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    if (callback != null) {
                        callback.onSuccess(sendResult);
                    }
                }

                @Override
                public void onException(Throwable e) {
                    log.error("send rocketmq failure, topic : {}, content:{}", topic, content, e);
                    if (callback != null) {
                        callback.onException(e);
                    }
                }
            });
        } catch (Exception e) {
            t.setStatus(e);
        } finally {
            t.complete();
        }
    }

}
