package cn.bixin.sona.web.controller;


import cn.bixin.sona.api.room.SonaRoomQueryRemoteService;
import cn.bixin.sona.api.room.SonaRoomRemoteService;
import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.dto.*;
import cn.bixin.sona.request.EnterRoomRequest;
import cn.bixin.sona.request.OpenCloseRoomRequest;
import cn.bixin.sona.request.OperateRequest;
import cn.bixin.sona.web.request.*;
import cn.bixin.sona.web.response.*;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 房间管理
 */
@RestController
@RequestMapping("/sona/room")
public class SonaController {

    private static final Logger log = LoggerFactory.getLogger(SonaController.class);

    @DubboReference
    private SonaRoomRemoteService sonaRoomRemoteService;
    @DubboReference
    private SonaRoomQueryRemoteService sonaRoomQueryRemoteService;

    @PostMapping("/create")
    public Response<RoomDetailInfoVO> createRoom(@RequestBody CreateRoomRequest request) {
        log.info("createRoom request:{}", JSON.toJSONString(request));

        cn.bixin.sona.request.CreateRoomRequest createRoomRequest = new cn.bixin.sona.request.CreateRoomRequest();
        createRoomRequest.setUid(Long.valueOf(request.getUid()));
        createRoomRequest.setName(request.getRoomTitle());
        createRoomRequest.setProductCode(request.getProductCode());
        createRoomRequest.setPassword(request.getPassword());
        createRoomRequest.setExtMap(request.getExtMap());

        Response<RoomDetailInfoDTO> response = sonaRoomRemoteService.createRoom(createRoomRequest);
        if (!response.isSuccess()) {
            log.error("createRoom error. e:{}", JSON.toJSONString(response));
            return Response.success(null);
        }

        return Response.success(convert(response.getResult()));
    }

    @PostMapping("/enter")
    public Response<RoomDetailInfoVO> enterRoom(@RequestBody EnterRequest request) {
        log.info("enterRoom request:{}", JSON.toJSONString(request));

        EnterRoomRequest enterRoomRequest = new EnterRoomRequest();
        enterRoomRequest.setRoomId(Long.valueOf(request.getRoomId()));
        enterRoomRequest.setUid(Long.valueOf(request.getUid()));
        enterRoomRequest.setPassword(request.getPassword());
        enterRoomRequest.setExtMap(request.getExtMap());

        Response<RoomDetailInfoDTO> response = sonaRoomRemoteService.enterRoom(enterRoomRequest);
        if (!response.isSuccess()) {
            log.error("enterRoom error. e:{}", JSON.toJSONString(response));
            return Response.fail(response.getCode(), response.getMsg());
        }

        return Response.success(convert(response.getResult()));
    }

    @PostMapping("/open")
    public Response<RoomDetailInfoVO> openRoom(@RequestBody OpenRoomRequest request) {
        log.info("openRoom request:{}", JSON.toJSONString(request));

        OpenCloseRoomRequest openRequest = new OpenCloseRoomRequest();
        openRequest.setRoomId(Long.valueOf(request.getRoomId()));
        openRequest.setUid(Long.valueOf(request.getUid()));
        Response<RoomDetailInfoDTO> response = sonaRoomRemoteService.openRoom(openRequest);
        if (!response.isSuccess()) {
            log.error("openRoom error. e:{}", JSON.toJSONString(response));
            return Response.fail(response.getCode(), response.getMsg());
        }

        return Response.success(convert(response.getResult()));
    }

    private RoomDetailInfoVO convert(RoomDetailInfoDTO dto) {
        ProductConfigInfoVO productConfigInfoVO = new ProductConfigInfoVO();

        ProductConfigInfoDTO productConfigInfoDTO = dto.getProductConfig();
        ImConfigInfoVO imConfigInfoVO = new ImConfigInfoVO();
        if (productConfigInfoDTO.getImConfig() != null) {
            ImConfigInfoDTO imConfigInfoDTO = productConfigInfoDTO.getImConfig();
            imConfigInfoVO.setModule(imConfigInfoDTO.getModule());
            imConfigInfoVO.setMessageExpireTime(imConfigInfoDTO.getMessageExpireTime());
            imConfigInfoVO.setClientQueueSize(imConfigInfoDTO.getClientQueueSize());
            imConfigInfoVO.setArrivalMessageSwitch(imConfigInfoDTO.isArrivalMessageSwitch());
            imConfigInfoVO.setImSendType(imConfigInfoDTO.getImSendType());
            productConfigInfoVO.setImConfig(imConfigInfoVO);
        }

        StreamConfigInfoVO streamConfigInfoVO = new StreamConfigInfoVO();
        if (productConfigInfoDTO.getStreamConfig() != null) {
            BeanUtils.copyProperties(productConfigInfoDTO.getStreamConfig(), streamConfigInfoVO);

            AppInfoDTO appInfoDTO = productConfigInfoDTO.getStreamConfig().getAppInfo();
            if (appInfoDTO != null) {
                AppInfoVO appInfoVO = new AppInfoVO();
                appInfoVO.setAppId(appInfoDTO.getAppId());
                appInfoVO.setAppSign(appInfoDTO.getAppSign());
                appInfoVO.setToken(appInfoDTO.getToken());
                appInfoVO.setAppID(appInfoDTO.getAppID());

                streamConfigInfoVO.setAppInfo(appInfoVO);
            }
            productConfigInfoVO.setStreamConfig(streamConfigInfoVO);
        }


        productConfigInfoVO.setProductCode(productConfigInfoDTO.getProductCode());
        productConfigInfoVO.setProductCodeAlias(productConfigInfoDTO.getProductCodeAlias());

        RoomDetailInfoVO vo = new RoomDetailInfoVO();
        vo.setRoomId(String.valueOf(dto.getRoomId()));
        vo.setOwnerUid(String.valueOf(dto.getOwnerUid()));

        BeanUtils.copyProperties(dto, vo);
        vo.setProductConfig(productConfigInfoVO);
        vo.setExtra(dto.getExtra());

        return vo;
    }


    @PostMapping("/leave")
    public Response<Boolean> leaveRoom(@RequestBody LeaveRoomRequest request) {
        log.info("leaveRoom request:{}", JSON.toJSONString(request));

        cn.bixin.sona.request.LeaveRoomRequest leaveRoomRequest = new cn.bixin.sona.request.LeaveRoomRequest();
        leaveRoomRequest.setRoomId(Long.valueOf(request.getRoomId()));
        leaveRoomRequest.setUid(Long.valueOf(request.getUid()));
        Response<Boolean> response = sonaRoomRemoteService.leaveRoom(leaveRoomRequest);
        if (!response.isSuccess()) {
            log.error("leaveRoom error. e:{}", JSON.toJSONString(response));
            return Response.fail(response.getCode(), response.getMsg());
        }

        return Response.success(response.getResult());
    }

    @PostMapping("/close")
    public Response<Boolean> closeRoom(@RequestBody CloseRoomRequest request) {
        log.info("closeRoom request:{}", JSON.toJSONString(request));

        OpenCloseRoomRequest closeRoomRequest = new OpenCloseRoomRequest();
        closeRoomRequest.setRoomId(Long.valueOf(request.getRoomId()));
        closeRoomRequest.setUid(Long.valueOf(request.getUid()));
        Response<Boolean> response = sonaRoomRemoteService.closeRoom(closeRoomRequest);
        if (!response.isSuccess()) {
            log.error("closeRoom error. e:{}", JSON.toJSONString(response));
            return Response.fail(response.getCode(), response.getMsg());
        }

        return Response.success(response.getResult());
    }

    @PostMapping("/password/update")
    public Response<Boolean> updatePassword(@RequestBody UpdatePasswordRequest request) {
        log.info("updatePassword request:{}", JSON.toJSONString(request));

        cn.bixin.sona.request.UpdatePasswordRequest updatePasswordRequest = new cn.bixin.sona.request.UpdatePasswordRequest();
        updatePasswordRequest.setRoomId(Long.valueOf(request.getRoomId()));
        updatePasswordRequest.setOperatorUid(Long.valueOf(request.getUid()));
        updatePasswordRequest.setNewPassword(request.getNewPassword());
        updatePasswordRequest.setOldPassword(request.getOldPassword());

        Response<Boolean> response = sonaRoomRemoteService.updatePassword(updatePasswordRequest);
        if (!response.isSuccess()) {
            log.error("updatePassword error. e:{}", JSON.toJSONString(response));
            return Response.fail(response.getCode(), response.getMsg());
        }

        return Response.success(response.getResult());
    }

    @PostMapping("/kick")
    public Response<Boolean> kickUser(@RequestBody KickRequest request) {
        log.info("kickUser request:{}", JSON.toJSONString(request));

        OperateRequest operateRequest = new OperateRequest();
        operateRequest.setRoomId(request.getRoomId());
        operateRequest.setOperatorUid(request.getUid());
        operateRequest.setTargetUid(request.getTargetUid());
        Response<Boolean> response = sonaRoomRemoteService.kickUser(operateRequest);
        if (!response.isSuccess()) {
            log.error("kickUser error. e:{}", JSON.toJSONString(response));
            return Response.fail(response.getCode(), response.getMsg());
        }

        return Response.success(response.getResult());
    }

    @PostMapping("/mute")
    public Response<Boolean> muteUser(@RequestBody MuteUserRequest request) {
        log.info("muteUser request:{}", JSON.toJSONString(request));

        OperateRequest operateRequest = new OperateRequest();
        operateRequest.setRoomId(Long.valueOf(request.getRoomId()));
        operateRequest.setOperatorUid(Long.valueOf(request.getUid()));
        operateRequest.setMinutes(request.getMinute());
        operateRequest.setTargetUid(Long.valueOf(request.getTargetUid()));

        Response<Boolean> response = sonaRoomRemoteService.muteUser(operateRequest);
        if (!response.isSuccess()) {
            log.error("muteUser error. e:{}", JSON.toJSONString(response));
            return Response.fail(response.getCode(), response.getMsg());
        }

        return Response.success(response.getResult());
    }

    @PostMapping("/mute/cancel")
    public Response<Boolean> cancelMuteUser(@RequestBody MuteUserRequest request) {
        log.info("cancelMuteUser request:{}", JSON.toJSONString(request));

        OperateRequest operateRequest = new OperateRequest();
        operateRequest.setRoomId(Long.valueOf(request.getRoomId()));
        operateRequest.setOperatorUid(Long.valueOf(request.getUid()));
        operateRequest.setTargetUid(Long.valueOf(request.getTargetUid()));

        Response<Boolean> response = sonaRoomRemoteService.cancelMuteUser(operateRequest);
        if (!response.isSuccess()) {
            log.error("cancelMuteUser error. e:{}", JSON.toJSONString(response));
            return Response.fail(response.getCode(), response.getMsg());
        }

        return Response.success(response.getResult());
    }

    @PostMapping("/block")
    public Response<Boolean> blockUser(@RequestBody BlockUserRequest request) {
        log.info("blockUser request:{}", JSON.toJSONString(request));

        OperateRequest operateRequest = new OperateRequest();
        operateRequest.setRoomId(Long.valueOf(request.getRoomId()));
        operateRequest.setOperatorUid(Long.valueOf(request.getUid()));
        operateRequest.setTargetUid(Long.valueOf(request.getTargetUid()));
        operateRequest.setReason(request.getReason());

        Response<Boolean> response = sonaRoomRemoteService.blockUser(operateRequest);
        if (!response.isSuccess()) {
            log.error("blockUser error. e:{}", JSON.toJSONString(response));
            return Response.fail(response.getCode(), response.getMsg());
        }

        return Response.success(response.getResult());
    }

    @PostMapping("/block/cancel")
    public Response<Boolean> cancelBlockUser(@RequestBody BlockUserRequest request) {
        log.info("cancelBlockUser request:{}", JSON.toJSONString(request));

        OperateRequest operateRequest = new OperateRequest();
        operateRequest.setRoomId(Long.valueOf(request.getRoomId()));
        operateRequest.setOperatorUid(Long.valueOf(request.getUid()));
        operateRequest.setTargetUid(Long.valueOf(request.getTargetUid()));

        Response<Boolean> response = sonaRoomRemoteService.cancelBlockUser(operateRequest);
        if (!response.isSuccess()) {
            log.error("cancelBlockUser error. e:{}", JSON.toJSONString(response));
            return Response.fail(response.getCode(), response.getMsg());
        }

        return Response.success(response.getResult());
    }

    @PostMapping("/admin/set")
    public Response<Boolean> setAdmin(@RequestBody BlockUserRequest request) {
        log.info("setAdmin request:{}", JSON.toJSONString(request));

        OperateRequest operateRequest = new OperateRequest();
        operateRequest.setRoomId(Long.valueOf(request.getRoomId()));
        operateRequest.setOperatorUid(Long.valueOf(request.getUid()));
        operateRequest.setTargetUid(Long.valueOf(request.getTargetUid()));
        operateRequest.setReason(request.getReason());

        Response<Boolean> response = sonaRoomRemoteService.setAdmin(operateRequest);
        if (!response.isSuccess()) {
            log.error("setAdmin error. e:{}", JSON.toJSONString(response));
            return Response.fail(response.getCode(), response.getMsg());
        }

        return Response.success(response.getResult());
    }

    @PostMapping("/admin/cancel")
    public Response<Boolean> cancelSetAdmin(@RequestBody BlockUserRequest request) {
        log.info("cancelSetAdmin request:{}", JSON.toJSONString(request));

        OperateRequest operateRequest = new OperateRequest();
        operateRequest.setRoomId(Long.valueOf(request.getRoomId()));
        operateRequest.setOperatorUid(Long.valueOf(request.getUid()));
        operateRequest.setTargetUid(Long.valueOf(request.getTargetUid()));

        Response<Boolean> response = sonaRoomRemoteService.removeAdmin(operateRequest);
        if (!response.isSuccess()) {
            log.error("cancelSetAdmin error. e:{}", JSON.toJSONString(response));
            return Response.fail(response.getCode(), response.getMsg());
        }

        return Response.success(response.getResult());
    }

    /**
     * 获取房间人数
     */
    @GetMapping(path = {"/member/count"})
    public Response<Long> getRoomMemberCount(@RequestParam String roomId) {
        return sonaRoomQueryRemoteService.getRoomMemberCount(Long.valueOf(roomId));
    }

    /**
     * 获取房间列表
     */
    @GetMapping(path = {"/member/list"})
    public Response<PageResult<RoomUserVO>> getRoomMemberList(@RequestParam String roomId, @RequestParam String anchor, @RequestParam int limit) {
        Response<PageResult<RoomUserDTO>> response = sonaRoomQueryRemoteService.getRoomMemberList(Long.valueOf(roomId), anchor, limit);
        if (!response.isSuccess() || CollectionUtils.isEmpty(response.getResult().getList())) {
            log.error("getRoomMemberList error. {}", JSON.toJSONString(response));
            return Response.success(PageResult.newPageResult(Collections.emptyList(), true));
        }

        PageResult<RoomUserVO> pageResult = new PageResult<>();
        PageResult<RoomUserDTO> pageResultDTO = response.getResult();
        pageResult.setAnchor(pageResultDTO.getAnchor());
        List<RoomUserVO> list = Lists.newArrayList();
        pageResultDTO.getList().forEach(each -> {
            RoomUserVO vo = new RoomUserVO();
            vo.setUid(each.getUid() + "");
            vo.setRole(each.getRole());
            list.add(vo);
        });

        pageResult.setList(list);
        pageResult.setEnd(pageResultDTO.getEnd());

        return Response.success(pageResult);
    }

}
