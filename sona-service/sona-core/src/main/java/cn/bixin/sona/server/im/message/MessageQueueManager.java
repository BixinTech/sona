package cn.bixin.sona.server.im.message;

import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.common.spring.SpringApplicationContext;
import cn.bixin.sona.server.im.config.ApolloConfig;
import cn.bixin.sona.server.im.utils.MessageLog;
import com.dianping.cat.Cat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qinwei
 * <p>
 * 根据业务隔离
 */
public class MessageQueueManager {

    private static final Map<String, MessageQueueService> QUEUE_SERVICES = new ConcurrentHashMap<>();

    public static void start(RoomMessageRequest request) {
        MessageLog messageLog = SpringApplicationContext.getBean(MessageLog.class);
        // 检查消息延迟时间
        if (checkDelay(request)) {
            Cat.logEvent(request.getPriority().name() + "_DISCARD", request.getProductCode() + ":" + request.getRoomId());
            messageLog.saveMessageLog(request, "DISCARD");
            return;
        }
        messageLog.saveMessageLog(request, "DELAY");
        // 处理请求
        getMessageQueueService(request.getProductCode()).start(request);
    }

    public static void stop() {
        QUEUE_SERVICES.forEach((key, value) -> value.stop());
    }

    private static MessageQueueService getMessageQueueService(String businessType) {
        return QUEUE_SERVICES.computeIfAbsent(businessType, MessageQueueService::new);
    }

    private static boolean checkDelay(RoomMessageRequest request) {
        Map<String, Long> delayConfig = SpringApplicationContext.getBean(ApolloConfig.class).getDelayConfig();
        return System.currentTimeMillis() - request.getSendTime() > delayConfig.getOrDefault(request.getPriority().name(), 1000L);
    }


}
