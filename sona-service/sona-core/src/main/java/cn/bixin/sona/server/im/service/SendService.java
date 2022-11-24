package cn.bixin.sona.server.im.service;

import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.api.socket.RoomMessageRemoteService;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.common.exception.RpcExceptionCode;
import cn.bixin.sona.server.im.config.ApolloConfig;
import cn.bixin.sona.server.im.flow.FlowConfig;
import cn.bixin.sona.server.im.flow.FlowControl;
import cn.bixin.sona.server.im.flow.FlowStrategy;
import cn.bixin.sona.server.im.message.MessageQueueManager;
import cn.bixin.sona.server.im.utils.ConvertUtils;
import cn.bixin.sona.server.im.utils.MessageLog;
import cn.bixin.sona.server.mq.KafkaSender;
import com.dianping.cat.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author qinwei
 */
@Service
public class SendService {

    private static final Logger log = LoggerFactory.getLogger(SendService.class);

    public static final String EXECUTOR = "-EXECUTOR";

    @Resource
    private RoomMessageRemoteService roomMessageRemoteService;

    @Resource
    private MessageLog messageLog;

    @Resource
    private KafkaSender kafkaSender;

    @Resource
    private FlowControl flowControl;

    @Resource
    private ApolloConfig apolloConfig;

    @Autowired
    private Map<String, Executor> executors;

    @Resource
    private Executor commonExecutor;

    public CompletableFuture<Void> sendMessage(RoomMessageRequest request) {
        return CompletableFuture.runAsync(() -> trySendMessage(request), executors.getOrDefault(request.getProductCode() + EXECUTOR, commonExecutor))
                .exceptionally(throwable -> handleException(request, throwable));
    }

    /**
     * 没有被频控，发送消息 ，否则走 fallback
     */
    private void trySendMessage(RoomMessageRequest request) {
        if (FlowStrategy.PASS == getFlowStrategy(request)) {
            sendChatRoomMessage(request);
        } else {
            MessageQueueManager.start(request);
        }
    }

    private Void handleException(RoomMessageRequest request, Throwable throwable) {
        if (RpcExceptionCode.TIMEOUT_EXCEPTION.getCode().equals(throwable.getMessage())) {
            MessageQueueManager.start(request);
        } else {
            Cat.logEvent("FAILURE", request.getProductCode() + ":" + request.getRoomId());
            messageLog.saveMessageLog(request, throwable.getMessage());
        }
        return null;
    }

    private FlowStrategy getFlowStrategy(RoomMessageRequest request) {
        Map<String, FlowConfig> flowConfig = apolloConfig.getFlowConfig();
        String key = "{" + request.getRoomId() + "}";
        return flowControl.throttle(key, flowConfig.get(request.getPriority().name()));
    }

    /**
     * 发送房间消息
     */
    private void sendChatRoomMessage(RoomMessageRequest request) {
        Response<Boolean> response = roomMessageRemoteService.sendChatroom(ConvertUtils.convertChatroomMsgRequest(request));
        // 如果发送成功
        if (response.isSuccess()) {
            saveMessage(request);
            return;
        }
        log.error("send room message failure , message:{} , response: {}", request.getContent(), response);
        throw new RuntimeException(response.getCode());
    }

    private void saveMessage(RoomMessageRequest request) {
        if (!request.isNeedToSave()) {
            return;
        }
        kafkaSender.send("TOPIC-ROOM-MESSAGE-RECORDER", ConvertUtils.convertChatSaveMessage(request));
    }

}
