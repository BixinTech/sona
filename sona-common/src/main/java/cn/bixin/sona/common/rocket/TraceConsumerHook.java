package cn.bixin.sona.common.rocket;

import cn.bixin.sona.common.trace.TraceHelper;
import cn.bixin.sona.common.trace.TracerContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * @author qinwei
 */
@Order
@Aspect
public class TraceConsumerHook {

    @Around("execution (* org.apache.rocketmq.spring.core.RocketMQListener.onMessage(..))")
    public Object intercept(ProceedingJoinPoint pjp) throws Throwable {
        MessageExt messageExt = Arrays.stream(pjp.getArgs())
                .filter(MessageExt.class::isInstance)
                .findFirst()
                .map(MessageExt.class::cast).orElseGet(null);
        if (messageExt == null) {
            return pjp.proceed();
        }
        String traceId = messageExt.getProperties().get(TracerContext.TRACER_TRACE_ID);
        if (StringUtils.hasText(traceId)) {
            TraceHelper.setTraceId(traceId);
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
