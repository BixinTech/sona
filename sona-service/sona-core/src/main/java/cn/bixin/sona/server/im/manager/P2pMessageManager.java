package cn.bixin.sona.server.im.manager;

import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.server.im.service.MercurySendService;
import cn.bixin.sona.server.im.service.SaveMessageService;
import cn.bixin.sona.server.im.utils.MessageLog;
import com.dianping.cat.Cat;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qinwei
 */
@Service
public class P2pMessageManager {

    private static final String P2P_MESSAGE_TOTAL = "P2P_MESSAGE_TOTAL";

    @Resource
    private MercurySendService mercurySendService;

    @Resource
    private SaveMessageService saveMessageService;

    @Resource
    private MessageLog messageLog;

    /**
     * 只有长连支持房间点对点消息
     */
    public Response<Boolean> sendP2pMessage(RoomMessageRequest request, List<Long> uids) {
        Map<String, String> tags = new HashMap<>();
        tags.put("productCode", request.getProductCode());
        tags.put("priority", request.getPriority().name());
        tags.put("type", request.getMsgType());
        Cat.logMetricForCount(P2P_MESSAGE_TOTAL, 1, tags);

        messageLog.saveMessageLog(request, request.getContent(), uids);
        return sendMessage(request, uids);
    }

    private Response<Boolean> sendMessage(RoomMessageRequest request, List<Long> uids) {
        if (CollectionUtils.isEmpty(request.getAckUids())) {
            request.setAckUids(uids);
        }
        Response<Boolean> response = mercurySendService.sendP2pMessage(request, uids);
        if (response.isSuccess()) {
            saveMessageService.saveRoomMessage(request);
        } else {
            messageLog.saveMessageLog(request, response.getMsg());
        }
        return response;
    }
}
