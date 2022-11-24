package cn.bixin.sona.web.controller;

import cn.bixin.sona.api.im.MessageCallbackService;
import cn.bixin.sona.api.im.MessageQueryService;
import cn.bixin.sona.api.im.dto.MessageInfoDTO;
import cn.bixin.sona.api.im.request.ChatroomMessageRequest;
import cn.bixin.sona.api.im.request.MessageQueryRequest;
import cn.bixin.sona.common.dto.Code;
import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.web.request.MessageRequest;
import cn.bixin.sona.web.response.MessageInfoVO;
import com.alibaba.fastjson.JSON;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 消息相关
 */
@RestController
@RequestMapping("/sona/message")
public class MessageController {

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    @DubboReference
    private MessageQueryService messageQueryService;

    @DubboReference
    private MessageCallbackService messageCallbackService;

    /**
     * 获取用户签名
     *
     * @return
     */
    @GetMapping("/query")
    public Response<PageResult<MessageInfoVO>> queryMessageList(@RequestParam String roomId,
                                                                @RequestParam String uid,
                                                                @RequestParam(required = false, defaultValue = "") String anchor,
                                                                @RequestParam(required = false, defaultValue = "20") int limit) {
        if (limit <= 0 || limit > 50) {
            limit = 20;
        }
        MessageQueryRequest request = new MessageQueryRequest();
        request.setRoomId(Long.parseLong(roomId));
        request.setUid(Long.parseLong(uid));
        request.setReserved(true);
        request.setAnchor(anchor);
        request.setLimit(limit);
        Response<PageResult<MessageInfoDTO>> response = messageQueryService.queryMessageInfo(request);
        if (!response.isSuccess()) {
            return Response.fail(Code.business(response.getCode(), response.getMsg()));
        }
        PageResult<MessageInfoDTO> page = response.getResult();
        if (page == null || CollectionUtils.isEmpty(page.getList())) {
            return Response.success(PageResult.newPageResult(new ArrayList<>(), true));
        }
        List<MessageInfoVO> resList = page.getList().stream()
                .filter(Objects::nonNull)
                .map(this::convertMessageInfoVO)
                .collect(Collectors.toList());
        PageResult<MessageInfoVO> resPage = PageResult.newPageResult(resList, page.getEnd(), page.getCount(), page.getAnchor());
        return Response.success(resPage);
    }

    private MessageInfoVO convertMessageInfoVO(MessageInfoDTO item) {
        MessageInfoVO result = new MessageInfoVO();
        result.setUid(String.valueOf(item.getUid()));
        result.setRoomId(String.valueOf(item.getRoomId()));
        result.setSendTime(item.getSendTime());
        result.setContent(item.getContent());
        result.setProductCode(item.getProductCode());
        result.setMessageId(item.getMessageId());
        return result;
    }

    @PostMapping("/send")
    public Response<Boolean> sendMessage(@RequestBody MessageRequest request) {
        log.info("send room message:{}", JSON.toJSONString(request));
        ChatroomMessageRequest messageRequest = new ChatroomMessageRequest();
        BeanUtils.copyProperties(request, messageRequest);
        return messageCallbackService.sendChatroomMessage(messageRequest);
    }

}
