package cn.bixin.sona.server.room.controller;

import cn.bixin.sona.api.room.StreamRemoteService;
import cn.bixin.sona.common.dto.Code;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.common.enums.PullMode;
import cn.bixin.sona.common.exception.YppRunTimeException;
import cn.bixin.sona.dto.AppInfoDTO;
import cn.bixin.sona.dto.RoomDTO;
import cn.bixin.sona.dto.SupplierConfigDTO;
import cn.bixin.sona.request.ChangeStreamRequest;
import cn.bixin.sona.request.InitStreamRequest;
import cn.bixin.sona.request.MixMVRequest;
import cn.bixin.sona.server.exception.ExceptionCode;
import cn.bixin.sona.server.room.domain.db.ProductConfig;
import cn.bixin.sona.server.room.domain.db.RoomConfig;
import cn.bixin.sona.server.room.domain.enums.StreamSupplierEnum;
import cn.bixin.sona.server.room.manager.StreamManager;
import cn.bixin.sona.server.room.service.ProductConfigService;
import cn.bixin.sona.server.room.service.RoomService;
import cn.bixin.sona.server.room.service.ZegoService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@DubboService
public class SteamRoomController implements StreamRemoteService {

    private static final Logger log = LoggerFactory.getLogger(SteamRoomController.class);

    @Resource
    private StreamManager streamManager;

    @Resource
    private RoomService roomService;

    @Resource
    private ProductConfigService productConfigService;

    @Resource
    private ZegoService zegoService;

    @Override
    public Response<String> initStream(InitStreamRequest request) {
        if (request.getUid() <= 0 || request.getRoomId() <= 0) {
            return Response.fail(ExceptionCode.ERROR_PARAM);
        }
        try {
            return Response.success(streamManager.initStream(request.getRoomId(), request.getUid()));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("createStream error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<String> getRoomStreamUrl(long roomId) {
        if (roomId <= 0) {
            return Response.fail(ExceptionCode.ERROR_PARAM);
        }
        try {
            return Response.success(streamManager.getRoomStreamUrl(roomId));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("getRoomStreamUrl error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Map<Long, String>> getRoomStreamUrlBatch(List<Long> roomIds) {
        if (CollectionUtils.isEmpty(roomIds) || roomIds.size() > 50) {
            return Response.fail(ExceptionCode.ERROR_PARAM);
        }
        try {
            return Response.success(streamManager.batchGetRoomStreamUrls(roomIds));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("getRoomStreamUrlBatch error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Boolean> muteStream(long roomId, List<Long> targetUids) {
        if (roomId <= 0 || CollectionUtils.isEmpty(targetUids)) {
            return Response.fail(ExceptionCode.ERROR_PARAM);
        }
        try {
            return Response.success(streamManager.muteStream(roomId, targetUids, true));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("muteStream error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Boolean> cancelMuteStream(long roomId, List<Long> targetUids) {
        if (roomId <= 0 || CollectionUtils.isEmpty(targetUids)) {
            return Response.fail(ExceptionCode.ERROR_PARAM);
        }
        try {
            return Response.success(streamManager.muteStream(roomId, targetUids, false));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("cancelMuteStream error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Boolean> muteRoomStream(long roomId) {
        if (roomId <= 0) {
            return Response.fail(ExceptionCode.ERROR_PARAM);
        }
        try {
            return Response.success(streamManager.muteRoomStream(roomId));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("muteRoomStream error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Boolean> addStream(ChangeStreamRequest request) {
        if (request == null || StringUtils.isBlank(request.getStreamId())) {
            return Response.fail(ExceptionCode.ERROR_PARAM);
        }
        try {
            return Response.success(streamManager.addStream(request));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("addStream error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Boolean> closeStream(ChangeStreamRequest request) {
        if (request == null || StringUtils.isBlank(request.getStreamId())) {
            return Response.fail(ExceptionCode.ERROR_PARAM);
        }
        try {
            return Response.success(streamManager.closeStream(request));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("closeStream error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Boolean> zegoReMix(long roomId) {
        log.info("zegoReMix, roomId: {}", roomId);
        RoomDTO roomDTO = roomService.getRoomByRoomId(roomId);
        if (roomDTO == null) {
            return Response.success(false);
        }
        RoomConfig roomConfig = productConfigService.getRoomConfig(roomId);
        String productCode = roomDTO.getProductCode();
        ProductConfig productConfig = roomConfig != null ? ProductConfig.convertConfigInfo(roomConfig, productCode) : productConfigService.getConfigInfoByCode(productCode);
        if (!StreamSupplierEnum.ZEGO.name().equals(productConfig.getStreamSupplier())) {
            return Response.success(false);
        }
        if (PullMode.MIXED.name().equals(productConfig.getPullMode())) {
            return Response.success(false);
        }
        zegoService.mix(roomId);
        return Response.success(true);
    }

    @Override
    public Response<AppInfoDTO> genUserSig(long roomId, long uid) {
        return Response.success(streamManager.genUserSig(roomId, uid));
    }


    @Override
    public Response<SupplierConfigDTO> syncRoomConfigByRoomId(long roomId) {
        return Response.success(streamManager.syncRoomConfig(roomId));
    }

    @Override
    public Response<Boolean> mixedMV(MixMVRequest request) {
        log.info("mixedMV request: {}", request);
        //关键参数不能为空
        if (Objects.isNull(request) || Objects.isNull(request.getRoomId()) || Objects.isNull(request.getMixStatus())) {
            Response.fail(Code.ERROR_PARAM);
        }

        //开启mv混流时, mv长度和宽度不能为空.
        if (Objects.equals(request.getMixStatus(), 1) && (Objects.isNull(request.getWidth()) || Objects.isNull(
                request.getHeight()))) {
            Response.fail(Code.ERROR_PARAM);
        }

        return Response.success(streamManager.mixedMV(request));
    }
}
