package cn.bixin.sona.web.controller;

import cn.bixin.sona.api.room.SonaStreamCallbackRemoteService;
import cn.bixin.sona.request.callback.CloseStreamCallback;
import cn.bixin.sona.request.callback.CreateReplayCallback;
import cn.bixin.sona.request.callback.CreateStreamCallback;
import cn.bixin.sona.web.request.callback.tencent.TxReplyRequest;
import cn.bixin.sona.web.request.callback.tencent.TxStreamRequest;
import com.alibaba.fastjson.JSON;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author dzsb-002293
 */
@RestController
@RequestMapping("/tencent")
public class TencentController {

    private static final Logger log = LoggerFactory.getLogger(TencentController.class);

    @DubboReference
    private SonaStreamCallbackRemoteService sonaStreamCallbackRemoteService;

    @PostMapping("/create")
    public String createStream(@RequestBody TxStreamRequest request) {
        log.info("tencent createStream callback: {}", JSON.toJSONString(request));
        CreateStreamCallback createStreamCallback = new CreateStreamCallback();
        createStreamCallback.setStreamId(request.getStreamId());
        createStreamCallback.setSource(2);
        sonaStreamCallbackRemoteService.handleCreateStreamCallback(createStreamCallback);
        return "{\"code\":0}";
    }

    @PostMapping("/close")
    public String closeStream(@RequestBody TxStreamRequest request) {
        log.info("tencent closeStream callback:{}", JSON.toJSONString(request));
        CloseStreamCallback closeStreamCallback = new CloseStreamCallback();
        closeStreamCallback.setStreamId(request.getStreamId());
        closeStreamCallback.setSource(2);
        sonaStreamCallbackRemoteService.handleCloseStreamCallback(closeStreamCallback);
        return "{\"code\":0}";
    }

    @PostMapping("/replay")
    public String saveReplay(@RequestBody TxReplyRequest request) {
        log.info("tencent replay callback. {}", JSON.toJSONString(request));
        if (request == null) {
            return "";
        }
        CreateReplayCallback callback = new CreateReplayCallback();
        callback.setSource(2);
        callback.setStreamId(request.getStreamId());
        callback.setBeginTime(new Date(request.getStartTime()));
        callback.setEndTime(new Date(request.getEndTime()));
        callback.setReplayUrl(request.getVideoUrl());
        sonaStreamCallbackRemoteService.handleCreateReplayCallback(callback);
        return "{\"code\":0}";
    }

}
