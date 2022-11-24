package cn.bixin.sona.web.controller;

import cn.bixin.sona.api.room.SonaStreamCallbackRemoteService;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.request.callback.CloseStreamCallback;
import cn.bixin.sona.request.callback.CreateReplayCallback;
import cn.bixin.sona.request.callback.CreateStreamCallback;
import cn.bixin.sona.web.request.callback.zego.ZegoCloseStreamRequest;
import cn.bixin.sona.web.request.callback.zego.ZegoCreateStreamRequest;
import cn.bixin.sona.web.request.callback.zego.ZegoSaveReplayRequest;
import com.alibaba.fastjson.JSON;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/zego")
public class ZegoController {

    private static final Logger log = LoggerFactory.getLogger(ZegoController.class);

    @DubboReference
    private SonaStreamCallbackRemoteService sonaStreamCallbackRemoteService;

    @PostMapping("/create")
    public String createStream(@RequestBody ZegoCreateStreamRequest request) {
        log.info("zego createStream callback: {}", JSON.toJSONString(request));
        String streamId = request.getStream_alias();

        if (!StringUtils.hasText(streamId)) {
            log.info("streamId is null: {}", JSON.toJSONString(request));
            return "0";
        }
        try {
            CreateStreamCallback callback = new CreateStreamCallback();
            callback.setStreamId(streamId);
            callback.setRoomId(Long.valueOf(request.getChannel_id()));
            callback.setSource(1);
            callback.setCreateTime(new Date(request.getCreate_time() * 1000L));
            callback.setHdlUrls(request.getHdl_url());
            callback.setHlsUrls(request.getHls_url());
            callback.setPicUrls(request.getPic_url());
            callback.setRtmpUrls(request.getRtmp_url());
            callback.setSdkAppId(request.getAppid());
            Response<Boolean> resp = sonaStreamCallbackRemoteService.handleCreateStreamCallback(callback);
            if (!resp.isSuccess()) {
                log.error("zego handleCreateStreamCallback failed: {}", resp);
                return "0";
            }
        } catch (Exception e) {
            log.warn("zego createStream error!", e);
        }
        return "1";
    }

    @PostMapping("/close")
    public String closeStream(@RequestBody ZegoCloseStreamRequest request) {
        log.info("zego closeStream callback: {}", request);
        String streamId = request.getStream_alias();
        if (!StringUtils.hasText(streamId)) {
            return "0";
        }
        try {
            CloseStreamCallback callback = new CloseStreamCallback();
            callback.setStreamId(request.getStream_alias());
            callback.setCloseType(request.getType());
            callback.setBizRoomId(request.getChannel_id());
            callback.setRoomId(request.getChannel_id());
            callback.setSource(1);
            callback.setSdkAppId(request.getAppid());
            Response<Boolean> resp = sonaStreamCallbackRemoteService.handleCloseStreamCallback(callback);
            if (!resp.isSuccess()) {
                log.error("zego handleCloseStreamCallback failed: {}", resp);
                return "0";
            }
        } catch (Exception e) {
            log.error("zego closeStream callback failed", e);
        }
        return "1";
    }

    @PostMapping("/replay")
    public String saveRelay(@RequestBody ZegoSaveReplayRequest request) {
        log.info("zego saveRelay callback: {}", request);
        String streamId = request.getStream_alias();
        if (!StringUtils.hasText(streamId)) {
            return "0";
        }

        CreateReplayCallback callback = new CreateReplayCallback();
        callback.setStreamId(request.getStream_alias());
        callback.setReplayUrl(request.getReplay_url());
        callback.setBeginTime(new Date(request.getBegin_time() * 1000L));
        callback.setEndTime(new Date(request.getEnd_time() * 1000L));
        callback.setSource(1);
        callback.setSdkAppId(request.getAppid());
        if (StringUtils.hasText(request.getExtra_params())) {
            callback.setExtraParams(JSON.parseObject(request.getExtra_params(), Map.class));
        }
        try {
            Response<Boolean> resp = sonaStreamCallbackRemoteService.handleCreateReplayCallback(callback);
            if (!resp.isSuccess()) {
                log.error("zego handleCreateReplayCallback failed: {}", resp);
                return "0";
            }
        } catch (Exception e) {
            log.error("zego saveRelay callback failed", e);
        }
        return "1";
    }

}
