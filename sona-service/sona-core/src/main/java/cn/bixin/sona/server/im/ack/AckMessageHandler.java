package cn.bixin.sona.server.im.ack;


import cn.bixin.sona.api.im.RouteAckMessageService;
import cn.bixin.sona.api.im.request.ChatroomMessageRequest;
import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.server.im.ack.data.MessageRequestWrap;
import io.netty.util.HashedWheelTimer;
import org.apache.dubbo.common.utils.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author qinwei
 */
@Component
public class AckMessageHandler {

    private static final Logger log = LoggerFactory.getLogger(AckMessageHandler.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private RouteAckMessageService routeAckMessageService;

    private static final HashedWheelTimer ACK_CHECK_TIMER = new HashedWheelTimer(new NamedThreadFactory("ack-check", true), 500, TimeUnit.MILLISECONDS, 512);

    public void handleAckMessage(ChatroomMessageRequest request) {
        if (!StringUtils.hasText(request.getMessageId())) {
            return;
        }
        redisTemplate.opsForSet().remove(getUnackMsgKey(request.getMessageId()), String.valueOf(request.getUid()));
    }

    /**
     * 消息必达处理
     */
    public void handleNeedAckMessage(MessageRequestWrap wrap) {
        RoomMessageRequest request = wrap.getRequest();
        if (CollectionUtils.isEmpty(request.getAckUids())) {
            return;
        }
        //存入需要确认的uid
        String unackKey = getUnackMsgKey(request.getMessageId());
        redisTemplate.opsForSet().add(unackKey, request.getAckUids().stream().map(String::valueOf).toArray(String[]::new));
        redisTemplate.expire(unackKey, 60, TimeUnit.SECONDS);
        //延迟检测
        ACK_CHECK_TIMER.newTimeout(timeout -> retrySendIfNeeded(wrap), 3, TimeUnit.SECONDS);
    }

    /**
     * 重发消息
     */
    private void retrySendIfNeeded(MessageRequestWrap wrap) {
        RoomMessageRequest request = wrap.getRequest();
        String messageId = request.getMessageId();
        // 获取未回复ack的用户
        Set<String> unAckMembers = redisTemplate.opsForSet().members(getUnackMsgKey(messageId));
        if (CollectionUtils.isEmpty(unAckMembers)) {
            log.info("AckMessage all ack success , messageId :{}", messageId);
            return;
        }
        // 达到最大重试次数
        if (wrap.getRetryCount() > 3) {
            log.info("AckMessage reach max retry , messageId :{} , lostRate : {} ,unack uids :{}", messageId, (double) unAckMembers.size() / request.getAckUids().size(), unAckMembers);
            return;
        }
        // 通过点对点模式发送
        request.setSendTime(System.currentTimeMillis());
        routeAckMessageService.sendAckMessage(request, unAckMembers.stream().map(Long::valueOf).collect(Collectors.toList()));
        // 未达到最大重试次数，继续延时检测
        int retryCount = wrap.getRetryCount() + 1;
        wrap.setRetryCount(retryCount);
        ACK_CHECK_TIMER.newTimeout(timeout -> retrySendIfNeeded(wrap), 3 * retryCount, TimeUnit.SECONDS);
    }

    private static String getUnackMsgKey(String messageId) {
        return "unack_" + messageId;
    }

}
