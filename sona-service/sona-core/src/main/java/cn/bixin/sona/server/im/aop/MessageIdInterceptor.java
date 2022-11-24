package cn.bixin.sona.server.im.aop;

import cn.bixin.sona.api.im.request.GroupMessageRequest;
import cn.bixin.sona.api.im.request.MessageRequest;
import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.common.util.IdGenerator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author qinwei
 */
@Component
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class MessageIdInterceptor {

    @Resource
    private IdGenerator idGenerator;

    @Before("execution(* cn.bixin.sona.server.im.controller.RouterRoomMessageController.*(..))")
    public void handleMessage(JoinPoint pjp) {
        Object[] args = pjp.getArgs();
        if (ObjectUtils.isEmpty(args)) {
            return;
        }
        for (Object arg : args) {
            if (arg instanceof List) {
                for (Object o : (List) arg) {
                    handleMessageId(o);
                }
            } else {
                handleMessageId(arg);
            }
        }
    }

    private void handleMessageId(Object arg) {
        if (!(arg instanceof MessageRequest)) {
            return;
        }
        MessageRequest request = (MessageRequest) arg;
        if (!StringUtils.hasText(request.getMessageId())) {
            request.setMessageId(idGenerator.strId());
        }
        JSONObject content = JSON.parseObject(request.getContent());
        content.put("messageId", request.getMessageId());
        if (request instanceof RoomMessageRequest) {
            content.put("roomId", ((RoomMessageRequest) request).getRoomId());
        } else if (request instanceof GroupMessageRequest) {
            content.put("roomId", ((GroupMessageRequest) request).getGroupId());
        }
        request.setContent(content.toJSONString());
    }

}
