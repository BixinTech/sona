package cn.bixin.sona.gateway.listener;

import cn.bixin.sona.gateway.util.NetUtil;
import com.ctrip.framework.apollo.ConfigService;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;

import java.util.List;

/**
 * @author qinwei
 */
@Slf4j
public abstract class BaseRocketMqListener {

    protected DefaultMQPushConsumer consumer;

    /**
     * 处理数据
     *
     * @param messageExt
     */
    protected abstract void handleMessage(MessageExt messageExt);

    protected DefaultMQPushConsumer initConsumer(String topic, ConsumeMode consumeMode) {
        String tag = getTag(NetUtil.LOCAL_IP_ADDR);
        String groupName = Joiner.on("_").join(topic, tag, "group");
        return initConsumer(topic, tag, groupName, consumeMode);
    }

    /**
     * 初始化消费者
     *
     * @param topic
     * @param tag
     * @param groupName
     * @return
     */
    protected DefaultMQPushConsumer initConsumer(String topic, String tag, String groupName, ConsumeMode consumeMode) {
        log.info("initConsumer(), topic={}, tag={}, groupName={}, consumeMode={}", topic, tag, groupName, consumeMode);
        consumer = new DefaultMQPushConsumer();
        consumer.setNamesrvAddr(ConfigService.getAppConfig().getProperty("rocketmq.name-server", ""));
        consumer.setConsumerGroup(groupName);
        consumer.setInstanceName(groupName);
        try {
            consumer.subscribe(topic, tag);
            log.info("topic of {} is listening and waiting mq start...", topic);
        } catch (MQClientException e) {
            log.error("{} rocketMq subscribe fail.[topic={}},Error={}", getClass().getSimpleName(), topic, e.getErrorMessage());
            return null;
        }

        if (consumeMode == ConsumeMode.CONCURRENTLY) {
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgList, ConsumeConcurrentlyContext context) {
                    for (MessageExt messageExt : msgList) {
                        Transaction transaction = Cat.newTransaction("RocketMQConsume", messageExt.getTopic());
                        try {
                            transaction.addData("msgId", messageExt.getMsgId());

                            handleMessage(messageExt);

                            transaction.setSuccessStatus();
                        } catch (Exception e) {
                            transaction.setStatus(e);
                            log.error("handle message failed from MQ", e);
                            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                        } finally {
                            transaction.complete();
                        }
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });

        } else if (consumeMode == ConsumeMode.ORDERLY) {
            consumer.registerMessageListener(new MessageListenerOrderly() {
                @Override
                public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgList, ConsumeOrderlyContext context) {
                    for (MessageExt messageExt : msgList) {
                        Transaction transaction = Cat.newTransaction("RocketMQConsume", messageExt.getTopic());
                        try {
                            transaction.addData("msgId", messageExt.getMsgId());

                            handleMessage(messageExt);

                            transaction.setSuccessStatus();
                        } catch (Exception e) {
                            transaction.setStatus(e);
                            log.error("handle message failed from MQ", e);
                            return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                        } finally {
                            transaction.complete();
                        }
                    }
                    return ConsumeOrderlyStatus.SUCCESS;
                }
            });

        }

        try {
            consumer.start();
        } catch (MQClientException e) {
            log.error("mq consumer start error!", e);
        }
        return consumer;
    }

    /**
     * 生成指定tag
     *
     * @param ip
     * @return
     */
    protected String getTag(String ip) {
        String ipStr = StringUtils.replace(ip, ".", "-");
        return Joiner.on("-").join("tag", ipStr);
    }


}
