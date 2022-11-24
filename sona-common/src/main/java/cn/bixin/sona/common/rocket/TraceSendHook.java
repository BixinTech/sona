package cn.bixin.sona.common.rocket;

import cn.bixin.sona.common.trace.TraceHelper;
import cn.bixin.sona.common.trace.TracerContext;
import org.apache.rocketmq.client.hook.SendMessageContext;
import org.apache.rocketmq.client.hook.SendMessageHook;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author qinwei
 */
public class TraceSendHook implements SendMessageHook, InitializingBean {

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Override
    public String hookName() {
        return "TraceSendHook";
    }

    @Override
    public void sendMessageBefore(SendMessageContext context) {
        context.getMessage().getProperties().put(TracerContext.TRACER_TRACE_ID, TraceHelper.getTraceId());
    }

    @Override
    public void sendMessageAfter(SendMessageContext context) {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        defaultMQProducer.getDefaultMQProducerImpl().registerSendMessageHook(this);
    }
}
