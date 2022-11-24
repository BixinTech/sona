package cn.bixin.sona.common.trace;

import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qinwei
 */
public class TracerContext {

    public static final String TRACER_TRACE_ID = "trace_id";

    private final Map<String, String> attachment = new ConcurrentHashMap<>();

    private static final ThreadLocal<TracerContext> CONTEXT_HOLDER = ThreadLocal.withInitial(TracerContext::new);

    private TracerContext() {
    }

    public void put(String key, String value) {
        if (key == null || value == null) {
            return;
        }
        attachment.put(key, value);
        MDC.put(key, value);
    }

    public String get(String key) {
        return key == null || key.isEmpty() ? null : attachment.get(key);
    }

    public Map<String, String> copyAttachment() {
        return new HashMap<>(attachment);
    }

    public static TracerContext getContext() {
        return CONTEXT_HOLDER.get();
    }

    public static void remove() {
        CONTEXT_HOLDER.remove();
        MDC.remove(TRACER_TRACE_ID);
    }
}
