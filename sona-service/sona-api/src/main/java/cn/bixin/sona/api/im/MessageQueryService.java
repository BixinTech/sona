package cn.bixin.sona.api.im;

import cn.bixin.sona.api.im.dto.MessageInfoDTO;
import cn.bixin.sona.api.im.request.MessageQueryRequest;
import cn.bixin.sona.common.annotation.CommonExecutor;
import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.common.dto.Response;

public interface MessageQueryService {

    @CommonExecutor(desc = "发送聊天室消息", printParam = true, printResponse = true)
    Response<PageResult<MessageInfoDTO>> queryMessageInfo(MessageQueryRequest request);

}
