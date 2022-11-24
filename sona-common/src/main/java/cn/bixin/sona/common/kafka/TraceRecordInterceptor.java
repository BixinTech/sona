package cn.bixin.sona.common.kafka;

import cn.bixin.sona.common.trace.TraceHelper;
import cn.bixin.sona.common.trace.TracerContext;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Arrays;

/**
 * @author qinwei
 */
@Order
@Aspect
public class TraceRecordInterceptor {

    @Around("@annotation(annotation)")
    public Object intercept(ProceedingJoinPoint pjp, KafkaListener annotation) throws Throwable {
        ConsumerRecord consumerRecord = Arrays.stream(pjp.getArgs())
                .filter(ConsumerRecord.class::isInstance)
                .findFirst()
                .map(ConsumerRecord.class::cast).orElseGet(null);
        if (consumerRecord == null) {
            return pjp.proceed();
        }
        Header header = consumerRecord.headers().lastHeader(TracerContext.TRACER_TRACE_ID);
        if (header != null) {
            TraceHelper.setTraceId(new String(header.value()));
        } else {
            TraceHelper.init();
        }
        try {
            return pjp.proceed();
        } finally {
            TraceHelper.reset();
        }
    }

}
