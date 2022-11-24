package cn.bixin.sona.server.im.message;

import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.server.im.service.SendService;
import cn.bixin.sona.common.spring.SpringApplicationContext;
import io.netty.util.HashedWheelTimer;
import org.apache.dubbo.common.utils.NamedThreadFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author qinwei
 */
public class MessageQueue {

    private final HashedWheelTimer delayTimer;

    private final SendService sendService;

    public MessageQueue(String name, String businessType) {
        this.sendService = SpringApplicationContext.getBean(SendService.class);
        this.delayTimer = new HashedWheelTimer(new NamedThreadFactory(businessType + "-" + name, true), 100, TimeUnit.MILLISECONDS, 512);
    }

    public void handle(RoomMessageRequest request) {
        delayTimer.newTimeout(timeout -> handleMessage(request), 500, TimeUnit.MILLISECONDS);
    }

    private void handleMessage(RoomMessageRequest request) {
        sendService.sendMessage(request);
    }

    public void close() {
        while (tasks() > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignore) {
            }
        }
        delayTimer.stop();
    }

    public long tasks() {
        return delayTimer.pendingTimeouts();
    }

}
