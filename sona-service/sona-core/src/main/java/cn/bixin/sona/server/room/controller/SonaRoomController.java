package cn.bixin.sona.server.room.controller;

import cn.bixin.sona.api.room.SonaRoomRemoteService;
import cn.bixin.sona.common.dto.Code;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.common.exception.YppRunTimeException;
import cn.bixin.sona.dto.RoomDetailInfoDTO;
import cn.bixin.sona.request.*;
import cn.bixin.sona.server.exception.ExceptionCode;
import cn.bixin.sona.server.room.domain.db.RoomLogRecord;
import cn.bixin.sona.server.room.domain.enums.OperateEnum;
import cn.bixin.sona.server.room.manager.RoomManager;
import com.alibaba.fastjson.JSON;
import com.dianping.cat.Cat;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

@DubboService
public class SonaRoomController implements SonaRoomRemoteService {

    private static final Logger log = LoggerFactory.getLogger(SonaRoomController.class);

    @Resource
    private RoomManager roomManager;

    @Override
    public Response<RoomDetailInfoDTO> createRoom(CreateRoomRequest request) {
        log.debug("createRoom request:{}", JSON.toJSONString(request));
        if (request == null || request.getProductCode() == null) {
            return Response.fail(ExceptionCode.EMPTY_PARAM);
        }

        try {
            return Response.success(roomManager.createRoom(request));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("createRoom error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<RoomDetailInfoDTO> openRoom(OpenCloseRoomRequest request) {
        log.debug("openRoom request:{}", JSON.toJSONString(request));
        if (request == null || request.getRoomId() == 0) {
            return Response.fail(ExceptionCode.EMPTY_PARAM);
        }

        try {
            return Response.success(roomManager.openRoom(request));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("openRoom error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Boolean> closeRoom(OpenCloseRoomRequest request) {
        log.debug("closeRoom request:{}", JSON.toJSONString(request));
        if (request == null || request.getRoomId() == 0) {
            return Response.fail(ExceptionCode.EMPTY_PARAM);
        }

        try {
            return Response.success(roomManager.closeRoom(request));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("closeRoom error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<RoomDetailInfoDTO> enterRoom(EnterRoomRequest request) {
        log.info("enterRoom request:{}", JSON.toJSONString(request));
        if (request == null || request.getRoomId() == 0) {
            return Response.fail(ExceptionCode.EMPTY_PARAM);
        }

        try {
            return Response.success(roomManager.enterRoom(request));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("enterRoom error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Boolean> leaveRoom(LeaveRoomRequest request) {
        log.info("leaveRoom request:{}", JSON.toJSONString(request));
        if (request == null || request.getRoomId() == 0) {
            return Response.fail(ExceptionCode.EMPTY_PARAM);
        }

        try {
            return Response.success(roomManager.leaveRoom(request));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("leaveRoom error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Boolean> updatePassword(UpdatePasswordRequest request) {
        log.info("updatePassword request:{}", JSON.toJSONString(request));
        if (request == null || request.getRoomId() == 0) {
            return Response.fail(ExceptionCode.EMPTY_PARAM);
        }

        try {
            return Response.success(roomManager.updatePassword(request));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("updatePassword error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Boolean> muteUser(OperateRequest request) {
        log.info("muteUser request:{}", JSON.toJSONString(request));
        if (request == null || request.getRoomId() == 0 || request.getOperatorUid() == 0 || request.getTargetUid() == 0) {
            return Response.fail(ExceptionCode.EMPTY_PARAM);
        }
        request.setOperate(OperateEnum.SET.getCode());

        try {
            return Response.success(roomManager.muteOrCancelUser(request));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("muteUser error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Boolean> cancelMuteUser(OperateRequest request) {
        log.info("cancelMuteUser request:{}", JSON.toJSONString(request));
        if (request == null || request.getRoomId() == 0 || request.getOperatorUid() == 0 || request.getTargetUid() == 0) {
            return Response.fail(ExceptionCode.EMPTY_PARAM);
        }
        request.setOperate(OperateEnum.CANCEL.getCode());

        try {
            return Response.success(roomManager.muteOrCancelUser(request));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("cancelMuteUser error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Boolean> blockUser(OperateRequest request) {
        log.info("blockUser request:{}", JSON.toJSONString(request));
        if (request == null || request.getRoomId() == 0 || request.getOperatorUid() == 0 || request.getTargetUid() == 0) {
            return Response.fail(ExceptionCode.EMPTY_PARAM);
        }
        request.setOperate(OperateEnum.SET.getCode());

        try {
            return Response.success(roomManager.blockOrCancelUser(request));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("blockUser error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Boolean> cancelBlockUser(OperateRequest request) {
        log.info("cancelBlockUser request:{}", JSON.toJSONString(request));
        if (request == null || request.getRoomId() == 0 || request.getOperatorUid() == 0 || request.getTargetUid() == 0) {
            return Response.fail(ExceptionCode.EMPTY_PARAM);
        }
        request.setOperate(OperateEnum.CANCEL.getCode());

        try {
            return Response.success(roomManager.blockOrCancelUser(request));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("cancelBlockUser error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Boolean> kickUser(OperateRequest request) {
        log.info("kickUser request:{}", JSON.toJSONString(request));
        if (request == null || request.getRoomId() == 0 || request.getOperatorUid() == 0 || request.getTargetUid() == 0) {
            return Response.fail(ExceptionCode.EMPTY_PARAM);
        }

        try {
            return Response.success(roomManager.kickUser(request));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("kickUser error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Boolean> setAdmin(OperateRequest request) {
        log.info("setAdmin request:{}", JSON.toJSONString(request));
        if (request == null || request.getRoomId() == 0 || request.getOperatorUid() == 0 || request.getTargetUid() == 0) {
            return Response.fail(ExceptionCode.EMPTY_PARAM);
        }
        request.setOperate(OperateEnum.SET.getCode());

        try {
            return Response.success(roomManager.setOrRemoveAdmin(request));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("setAdmin error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Boolean> removeAdmin(OperateRequest request) {
        log.info("removeAdmin request:{}", JSON.toJSONString(request));
        if (request == null || request.getRoomId() == 0 || request.getOperatorUid() == 0 || request.getTargetUid() == 0) {
            return Response.fail(ExceptionCode.EMPTY_PARAM);
        }
        request.setOperate(OperateEnum.CANCEL.getCode());

        try {
            return Response.success(roomManager.setOrRemoveAdmin(request));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("removeAdmin error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Boolean> updateChatroomUserScore(Long roomId, Long uid, int score) {
        log.info("updateChatroomUserScore roomId:{}, uid:{}, score:{}", roomId, uid, score);
        if (roomId == null || uid == 0) {
            return Response.fail(ExceptionCode.EMPTY_PARAM);
        }

        try {
            return Response.success(roomManager.updateChatroomUserScore(roomId, uid, score));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("updateChatroomUserScore error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Boolean> logReport(LogReportRequest request) {
        if (request == null || StringUtils.isBlank(request.getData())) {
            return Response.fail(Code.ERROR_PARAM);
        }
        RoomLogRecord logRecord = RoomLogRecord.convertRoomLogRecord(request);
        if (logRecord == null) {
            return Response.fail(Code.ERROR_PARAM);
        }
        //todo hermes log
//        businessManager.saveHermesLog(logRecord);
        Cat.logEvent("ErrorReport", Joiner.on(":").join(logRecord.getRoomId(), logRecord.getUid(), request.getCode()));
        return Response.success(true);
    }
}
