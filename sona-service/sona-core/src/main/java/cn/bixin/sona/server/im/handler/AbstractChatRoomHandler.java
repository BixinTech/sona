package cn.bixin.sona.server.im.handler;

import cn.bixin.sona.api.im.enums.PriorityEnum;
import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.server.im.config.ApolloConfig;
import cn.bixin.sona.server.im.flow.FlowConfig;
import cn.bixin.sona.server.im.flow.FlowControl;
import cn.bixin.sona.server.im.flow.FlowStrategy;
import cn.bixin.sona.server.im.manager.ChatRoomMessageManager;
import cn.bixin.sona.server.mq.KafkaSender;
import com.dianping.cat.Cat;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qinwei
 */
public abstract class AbstractChatRoomHandler implements ChatRoomHandler {

    public static final String CHATROOM_MESSAGE_TOTAL = "CHATROOM_MESSAGE_TOTAL";

    @Resource
    public FlowControl flowControl;

    @Resource
    public ApolloConfig apolloConfig;

    @Resource
    private KafkaSender kafkaSender;

    @Resource
    public ChatRoomMessageManager chatRoomMessageManager;

    @Override
    public Response<Boolean> handle(RoomMessageRequest request) {
        Map<String, String> tags = new HashMap<>();
        tags.put("productCode", request.getProductCode());
        tags.put("priority", request.getPriority().name());
        tags.put("type", request.getMsgType());
        Cat.logMetricForCount(CHATROOM_MESSAGE_TOTAL, 1, tags);

        return doHandle(request);
    }

    protected FlowStrategy getStrategy(RoomMessageRequest request) {
        PriorityEnum priority = request.getPriority();
        String key = "{" + request.getRoomId() + "}";
        Map<String, FlowConfig> flowConfig = apolloConfig.getFlowConfig();
        FlowConfig config = flowConfig.get(priority.name());
        if (PriorityEnum.LOW == request.getPriority() && config != null) {
            config.setDeduct(0);
        }
        FlowStrategy flowStrategy = flowControl.throttle(key, config);

        if (FlowStrategy.PASS != flowStrategy) {
            Cat.logEvent(priority.name() + "_LIMITER", request.getProductCode() + ":" + request.getRoomId());
        }
        return flowStrategy;
    }

    protected Response<Boolean> sendKafka(RoomMessageRequest request) {
        kafkaSender.send("TOPIC-ROOM-IM-MESSAGE", request);
        return Response.success(true);
    }

    protected abstract Response<Boolean> doHandle(RoomMessageRequest request);

}
