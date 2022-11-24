package cn.bixin.sona.server.im.aop;

import cn.bixin.sona.api.im.enums.PriorityEnum;
import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.server.im.ack.MessageArrivalService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * @author qinwei
 */
@Order
@Component
@Aspect
public class AckMessageInterceptor {

    @Resource
    private MessageArrivalService messageArrivalService;

    @Around("execution(* cn.bixin.sona.server.im.service.MercurySendService.sendP2pMessage(..))")
    public Object handleAckP2pMessage(ProceedingJoinPoint pjp) throws Throwable {
        Arrays.stream(pjp.getArgs())
                .filter(RoomMessageRequest.class::isInstance)
                .findFirst()
                .map(RoomMessageRequest.class::cast)
                .filter(request -> PriorityEnum.HIGH == request.getPriority())
                .ifPresent(request -> messageArrivalService.sendChatRoomAckMessage(request));
        return pjp.proceed();
    }

    @Around("execution(* cn.bixin.sona.server.im.service.MercurySendService.sendChatRoomMessage(..))")
    public Object handleAckRoomMessage(ProceedingJoinPoint pjp) throws Throwable {
        Arrays.stream(pjp.getArgs())
                .filter(RoomMessageRequest.class::isInstance)
                .findFirst()
                .map(RoomMessageRequest.class::cast)
                .filter(request -> PriorityEnum.HIGH == request.getPriority())
                .map(this::getChatRoomAck)
                .ifPresent(request -> messageArrivalService.sendChatRoomAckMessage(request));
        return pjp.proceed();
    }

    private RoomMessageRequest getChatRoomAck(RoomMessageRequest request) {
        Set<Long> ackUids = messageArrivalService.getNeedAckUids(request);
        if (CollectionUtils.isEmpty(ackUids)) {
            return null;
        }
        request.setAckUids(new ArrayList<>(ackUids));
        return request;
    }

}
