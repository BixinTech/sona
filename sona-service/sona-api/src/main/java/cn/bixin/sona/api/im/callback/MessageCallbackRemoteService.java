package cn.bixin.sona.api.im.callback;

import cn.bixin.sona.api.im.request.ChatroomMessageRequest;
import cn.bixin.sona.common.annotation.CommonExecutor;
import cn.bixin.sona.common.dto.Response;

/**
 * 在实现类上添加注解 @DubboService(group = "sona_${productcode}")
 * 通过 group 区分不同的业务方，可以在回调中对消息做风控或者消息内容的组装等。
 *
 * @author qinwei
 */
public interface MessageCallbackRemoteService {

    @CommonExecutor(desc = "消息回调业务方", printParam = true, printResponse = true)
    Response<ChatroomMessageRequest> checkMessageCallback(ChatroomMessageRequest request);

}
