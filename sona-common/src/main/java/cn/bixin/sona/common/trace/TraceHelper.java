package cn.bixin.sona.common.trace;

import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @author qinwei
 */
public class TraceHelper {

    public static TracerContext init() {
        TracerContext context = TracerContext.getContext();
        String traceId = context.get(TracerContext.TRACER_TRACE_ID);
        if (!StringUtils.hasText(traceId)) {
            traceId = generateTraceId();
            context.put(TracerContext.TRACER_TRACE_ID, traceId);
        }
        return context;
    }

    public static String getTraceId() {
        return init().get(TracerContext.TRACER_TRACE_ID);
    }

    public static void reset() {
        TracerContext.remove();
    }

    public static void runWithInitAndReset(Runnable runnable) {
        init();
        try {
            runnable.run();
        } finally {
            reset();
        }
    }

    public static void setTraceId(String traceId) {
        TracerContext.getContext().put(TracerContext.TRACER_TRACE_ID, traceId);
    }

    public static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
