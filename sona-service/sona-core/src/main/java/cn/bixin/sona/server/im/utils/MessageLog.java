package cn.bixin.sona.server.im.utils;

import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.common.trace.TraceHelper;
import cn.bixin.sona.server.mq.KafkaSender;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qinwei
 */
@Component
public class MessageLog {

    private static final String TOPIC_ROOM_IM_MESSAGE_LOG = "TOPIC-ROOM-IM-MESSAGE-LOG";

    @Resource
    private KafkaSender kafkaSender;

    public void saveMessageLog(RoomMessageRequest request, String content) {
        saveMessageLog(request, content, null);
    }

    public void saveMessageLog(String messageId, String content) {
        Map<String, Object> map = new HashMap<>(8);
        map.put("messageId", messageId);
        map.put("tid", TraceHelper.getTraceId());
        map.put("content", content);
        kafkaSender.send(TOPIC_ROOM_IM_MESSAGE_LOG, map);
    }

    public void saveMessageLog(RoomMessageRequest request, String content, List<Long> toUid) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("messageId", request.getMessageId());
        map.put("roomId", request.getRoomId());
        map.put("msgType", request.getMsgType());
        map.put("productCode", request.getProductCode());
        map.put("sendTime", request.getSendTime());
        map.put("priority", request.getPriority().name());
        map.put("uid", request.getUid());
        map.put("content", content);
        if (toUid != null) {
            map.put("toUid", JSON.toJSONString(toUid));
        }
        kafkaSender.send(TOPIC_ROOM_IM_MESSAGE_LOG, map);
    }

}
