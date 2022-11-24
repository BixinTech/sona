package cn.bixin.sona.web.controller;

import cn.bixin.sona.api.room.StreamRemoteService;
import cn.bixin.sona.common.dto.Code;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.dto.AppInfoDTO;
import cn.bixin.sona.dto.StreamConfigInfoDTO;
import cn.bixin.sona.dto.SupplierConfigDTO;
import cn.bixin.sona.request.MixMVRequest;
import cn.bixin.sona.web.request.MixedMVRequest;
import cn.bixin.sona.web.request.MuteStreamRequest;
import cn.bixin.sona.web.response.AppInfoVO;
import cn.bixin.sona.web.response.StreamConfigInfoVO;
import cn.bixin.sona.web.response.SupplierConfigVO;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * 流相关操作
 */
@RestController
@RequestMapping("/sona/stream")
public class StreamController {

    private static final Logger log = LoggerFactory.getLogger(StreamController.class);

    @DubboReference
    private StreamRemoteService streamRemoteService;

    /**
     * sync/config方法执行开关
     */
    @Value("${sona.sync.config:true}")
    private boolean isSyncConfig;
    /**
     * 房间ID 逗号分隔
     */
    @Value("${sona.sync.config.roomIds:}")
    private String syncRoomIds;


    /**
     * 获取用户签名
     * @return
     */
    @GetMapping("/gen/userSig")
    public Response<AppInfoVO> genUserSig(@RequestParam String roomId, @RequestParam String uid) {
        Response<AppInfoDTO> response = streamRemoteService.genUserSig(Long.valueOf(roomId), Long.valueOf(uid));
        if (!response.isSuccess()) {
            log.error("genUserSig error. {}", JSON.toJSONString(response));
            return Response.success(null);
        }

        AppInfoVO vo = new AppInfoVO();
        BeanUtils.copyProperties(response.getResult(), vo);
        return Response.success(vo);
    }

    /**
     * 静音
     */
    @PostMapping(path = {"/mute"})
    public Response<Boolean> muteStream(@RequestBody MuteStreamRequest request) {

        List<String> targetUids = request.getTargetUids();
        List<Long> uids = Lists.newArrayList();
        targetUids.forEach(uid -> uids.add(Long.valueOf(uid)));

        return streamRemoteService.muteStream(Long.parseLong(request.getRoomId()), uids);
    }

    /**
     * 取消静音
     */
    @PostMapping(path = {"/mute/cancel"})
    public Response<Boolean> cancelMuteStream(@RequestBody MuteStreamRequest request) {
        List<String> targetUids = request.getTargetUids();
        List<Long> uids = Lists.newArrayList();
        targetUids.forEach(uid -> uids.add(Long.valueOf(uid)));

        return streamRemoteService.cancelMuteStream(Long.parseLong(request.getRoomId()), uids);
    }

    @GetMapping("/sync/config")
    public Response<SupplierConfigVO> syncSupplierConfig(@RequestParam("roomId") String roomId, @RequestParam("uid") String uid) {
        //Apollo配置指定房间
       /* if (!StringUtils.isEmpty(syncRoomIds)) {
            String[] split = syncRoomIds.split(",");
            List<String> roomList = Lists.newArrayList(split);
            if (roomList.contains(roomId)) {
                return Response.success(null);
            }
        }*/

        if (!StringUtils.hasText(roomId)) {
            return Response.fail(Code.ERROR_PARAM);
        }

        Response<SupplierConfigDTO> response = streamRemoteService.syncRoomConfigByRoomId(Long.valueOf(roomId));
        if (!response.isSuccess() || response.getResult() == null) {
            return Response.success(null);
        }

        SupplierConfigDTO configDTO = response.getResult();

        SupplierConfigVO vo = new SupplierConfigVO();
        vo.setRoomId(configDTO.getRoomId());

        StreamConfigInfoDTO streamConfigDTO = configDTO.getStreamConfig();
        StreamConfigInfoVO streamConfigVO = new StreamConfigInfoVO();
        streamConfigVO.setBitrate(streamConfigDTO.getBitrate());
        streamConfigVO.setSupplier(streamConfigDTO.getSupplier());
        streamConfigVO.setType(streamConfigDTO.getType());
        streamConfigVO.setPushMode(streamConfigDTO.getPushMode());
        streamConfigVO.setPullMode(streamConfigDTO.getPullMode());
        streamConfigVO.setStreamList(streamConfigDTO.getStreamList());
        streamConfigVO.setStreamUrl(streamConfigDTO.getStreamUrl());
        streamConfigVO.setStreamId(streamConfigDTO.getStreamId());
        streamConfigVO.setAudioToken(streamConfigDTO.getAudioToken());
        streamConfigVO.setStreamRoomId(streamConfigDTO.getStreamRoomId());
        streamConfigVO.setSwitchSpeaker(streamConfigDTO.getSwitchSpeaker());

        AppInfoDTO appInfoDTO = streamConfigDTO.getAppInfo();
        if (appInfoDTO != null) {
            AppInfoVO appInfo = new AppInfoVO();
            appInfo.setAppId(appInfoDTO.getAppId());
            appInfo.setAppSign(appInfoDTO.getAppSign());
            appInfo.setToken(appInfoDTO.getToken());
            appInfo.setAppID(appInfoDTO.getAppID());

            streamConfigVO.setAppInfo(appInfo);
        }

        vo.setStreamConfig(streamConfigVO);


        return Response.success(vo);
    }

    /**
     * 是否混入mv
     * 根据 mixStatus 区分 开始/结束 混入MV
     */
    @PostMapping(path = "/mixed/mv")
    public Response<Boolean> mixedMV(@RequestBody MixedMVRequest request) {
        log.info("mixedMV request: {}, mobileContext: {}", request);
        if (Objects.isNull(request) || Objects.isNull(request.getMixStatus()) || Objects.isNull(request.getRoomId())
            || Objects.isNull(request.getUid())) {
            return Response.fail(Code.ERROR_PARAM);
        }

        MixMVRequest remoteRequest = new MixMVRequest();
        remoteRequest.setRoomId(Long.valueOf(request.getRoomId()));
        remoteRequest.setUid(Long.valueOf(request.getUid()));
        remoteRequest.setMixStatus(request.getMixStatus());
        remoteRequest.setWidth(request.getWidth());
        remoteRequest.setHeight(request.getHeight());

        return streamRemoteService.mixedMV(remoteRequest);
    }


}
