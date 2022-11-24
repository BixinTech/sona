package cn.bixin.sona.server.im.controller;

import cn.bixin.sona.api.im.MessageQueryService;
import cn.bixin.sona.api.im.dto.MessageInfoDTO;
import cn.bixin.sona.api.im.request.MessageQueryRequest;
import cn.bixin.sona.common.dto.Code;
import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.server.im.manager.MessageQueryManager;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class MessageQueryController implements MessageQueryService {

    @Resource
    MessageQueryManager messageQueryManager;

    @Override
    public Response<PageResult<MessageInfoDTO>> queryMessageInfo(MessageQueryRequest request) {
        if (request == null || request.getRoomId() <= 0) {
            return Response.fail(Code.ERROR_PARAM);
        }
        int limit = request.getLimit();
        if (limit <= 0 || limit > 50) {
            limit = 20;
        }
        PageResult<MessageInfoDTO> pageResult = messageQueryManager.queryMessageInfoPage(request.getUid(), request.getRoomId(), request.isReserved(), request.getAnchor(), limit);
        return Response.success(pageResult);
    }
}
