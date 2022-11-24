package cn.bixin.sona.server.im.message;

import cn.bixin.sona.api.im.request.RoomMessageRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qinwei
 * <p>
 * 根据消息优先级隔离
 */
public class MessageQueueService {

    private final Map<String, MessageQueue> queues = new ConcurrentHashMap<>();

    private final String businessType;

    public MessageQueueService(String businessType) {
        this.businessType = businessType;
    }

    public void start(RoomMessageRequest request) {
        queues.computeIfAbsent(request.getPriority().name(), name -> new MessageQueue(name, businessType)).handle(request);
    }

    public void stop() {
        queues.values().forEach(MessageQueue::close);
    }

}
