package cn.bixin.sona.console.controller;

import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.console.domain.dto.ChatroomStreamDTO;
import cn.bixin.sona.console.domain.req.StreamQueryRequest;
import cn.bixin.sona.console.manager.StreamManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@CrossOrigin
@Slf4j
@RestController
public class StreamRecordController {

    @Resource
    private StreamManager streamManager;

    /**
     * 流录制查询单流
     *
     * @param request 参数
     * @return
     */
    @PostMapping("/stream/record/multi")
    public Response<PageResult<ChatroomStreamDTO>> queryChatroomMultiStream(@RequestBody StreamQueryRequest request) {
        return Response.success(streamManager.queryChatroomStreamRecord(request));
    }

    /**
     * 流录制查询混流
     *
     * @param request 参数
     * @return
     */
    @PostMapping("/stream/record/mixed")
    Response<PageResult<ChatroomStreamDTO>> queryChatroomMixStream(@RequestBody StreamQueryRequest request) {
        return Response.success(streamManager.queryChatroomMixStream(request));
    }

    /**
     * 流录制查询流地址
     *
     * @param request 参数
     * @return
     */
    @PostMapping("/stream/record/addr")
    Response<PageResult<ChatroomStreamDTO>> queryChatroomStream(@RequestBody StreamQueryRequest request) {
        return Response.success(streamManager.queryChatroomStream(request));
    }
}
