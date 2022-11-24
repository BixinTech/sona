package cn.bixin.sona.session.mq;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
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

    public SendResult syncSend(String topic, String content) {
        return syncSend(topic, null, content);
    }

    public SendResult syncSend(String topic, String tag, String content) {
        return syncSend(topic, tag, null, content);
    }

    public SendResult syncSend(String topic, String tag, String hashKey, String content) {
        String destination = tag == null ? topic : topic + ':' + tag;
        Transaction t = Cat.newTransaction("RocketMQ", destination);
        try {
            MessageBuilder<?> builder = MessageBuilder.withPayload(content);
            if (hashKey == null) {
                return rocketMQTemplate.syncSendOrderly(destination, builder.build(), hashKey);
            } else {
                return rocketMQTemplate.syncSend(destination, builder.build());
            }
        } catch (Exception e) {
            log.error("send rocketmq failure, destination : {}, content:{}", destination, content, e);
            t.setStatus(e);
        } finally {
            t.complete();
        }
        return null;
    }

    public void asyncSend(String topic, String content) {
        asyncSend(topic, content, null);
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
