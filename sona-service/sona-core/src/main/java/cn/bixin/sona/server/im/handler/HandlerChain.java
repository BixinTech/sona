package cn.bixin.sona.server.im.handler;

import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.server.im.utils.MessageLog;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qinwei
 */
@Component
public class HandlerChain implements ApplicationContextAware {

    private List<ChatRoomHandler> handlers;

    @Resource
    private MessageLog messageLog;

    public Response<Boolean> handle(RoomMessageRequest request) {
        messageLog.saveMessageLog(request, request.getContent());
        for (ChatRoomHandler handler : handlers) {
            if (handler.support(request)) {
                Response<Boolean> response = handler.handle(request);
                if (!response.isSuccess()) {
                    messageLog.saveMessageLog(request, response.getMsg());
                    return response;
                }
            }
        }
        return Response.success(true);
    }

    @Async("roomMsgExecutor")
    public void handle(List<RoomMessageRequest> list) {
        for (RoomMessageRequest request : list) {
            handle(request);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        handlers = applicationContext.getBeansOfType(ChatRoomHandler.class).values().stream().sorted(Comparator.comparingInt(ChatRoomHandler::order)).collect(Collectors.toList());
    }
}
