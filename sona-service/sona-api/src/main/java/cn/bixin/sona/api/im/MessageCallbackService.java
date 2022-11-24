package cn.bixin.sona.api.im;


import cn.bixin.sona.api.im.request.ChatroomMessageRequest;
import cn.bixin.sona.common.annotation.CommonExecutor;
import cn.bixin.sona.common.dto.Response;

import javax.validation.constraints.NotNull;

/**
 * 客户端通过 http 或者 长链 发送消息，需要回调对应的业务方，判断是否可以发送
 *
 * @author qinwei
 */
public interface MessageCallbackService {

    @CommonExecutor(desc = "客户端发送房间消息", printParam = true, printResponse = true)
    Response<Boolean> sendChatroomMessage(@NotNull ChatroomMessageRequest request);

}
