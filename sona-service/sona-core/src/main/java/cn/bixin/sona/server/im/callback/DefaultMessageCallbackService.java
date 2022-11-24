package cn.bixin.sona.server.im.callback;

import cn.bixin.sona.api.im.callback.MessageCallbackRemoteService;
import cn.bixin.sona.api.im.request.ChatroomMessageRequest;
import cn.bixin.sona.common.dto.Response;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author qinwei
 */
@DubboService(group = "default")
public class DefaultMessageCallbackService implements MessageCallbackRemoteService {

    @Override
    public Response<ChatroomMessageRequest> checkMessageCallback(ChatroomMessageRequest request) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("roomId", request.getRoomId());
        jsonObject.put("msgType", request.getMsgType());
        jsonObject.put("data", JSON.parseObject(request.getContent()));
        request.setContent(jsonObject.toJSONString());
        return Response.success(request);
    }

}
