package cn.bixin.sona.server.im.manager;

import cn.bixin.sona.api.im.request.GroupMessageRequest;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.server.im.service.MercurySendService;
import cn.bixin.sona.server.im.service.SaveMessageService;
import com.dianping.cat.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qinwei
 */
@Service
public class GroupMessageManager {

    private static final Logger log = LoggerFactory.getLogger(GroupMessageManager.class);

    private static final String GROUP_MESSAGE_TOTAL = "GROUP_MESSAGE_TOTAL";

    @Resource
    private MercurySendService mercurySendService;

    @Resource
    private SaveMessageService saveMessageService;

    public Response<Boolean> sentGroupMessage(GroupMessageRequest request) {
        // cat 埋点
        Map<String, String> tags = new HashMap<>();
        tags.put("productCode", request.getProductCode());
        tags.put("group", String.valueOf(request.getGroupId()));
        Cat.logMetricForCount(GROUP_MESSAGE_TOTAL, 1, tags);

        Response<Boolean> response = mercurySendService.sendGroupMessage(request);
        if (response.isSuccess()) {
            saveMessageService.saveGroupMessage(request);
        }
        return response;
    }

    @Async("groupMsgExecutor")
    public void sentGroupMessages(List<GroupMessageRequest> list) {
        for (GroupMessageRequest request : list) {
            sentGroupMessage(request);
        }
    }

}
