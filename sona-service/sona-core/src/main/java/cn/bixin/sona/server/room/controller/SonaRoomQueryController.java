package cn.bixin.sona.server.room.controller;

import cn.bixin.sona.api.room.SonaRoomQueryRemoteService;
import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.common.exception.YppRunTimeException;
import cn.bixin.sona.dto.RoomConfigDTO;
import cn.bixin.sona.dto.RoomDTO;
import cn.bixin.sona.dto.RoomUserDTO;
import cn.bixin.sona.server.exception.ExceptionCode;
import cn.bixin.sona.server.room.manager.RoomManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@DubboService
public class SonaRoomQueryController implements SonaRoomQueryRemoteService {

    private static final Logger log = LoggerFactory.getLogger(SonaRoomQueryController.class);

    @Resource
    private RoomManager roomManager;

    @Override
    public Response<RoomDTO> getRoom(Long roomId) {
        return Response.success(roomManager.getRoom(roomId));
    }

    @Override
    public Response<Map<Long, RoomDTO>> getRoomBatch(List<Long> roomIds) {
        if (CollectionUtils.isEmpty(roomIds) || roomIds.size() > 50) {
            return Response.fail(ExceptionCode.ERROR_PARAM);
        }
        return Response.success(roomManager.getRoomBatch(roomIds));
    }

    @Override
    public Response<Long> getRoomMemberCount(long roomId) {
        try {
            return Response.success(roomManager.getRoomMemberCount(roomId));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("getRoomMemberCount error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<Map<Long, Long>> batchGetRoomMemberCount(List<Long> roomIds) {
        if (CollectionUtils.isEmpty(roomIds) || roomIds.size() > 50) {
            return Response.fail(ExceptionCode.ERROR_PARAM);
        }
        try {
            return Response.success(roomManager.batchGetRoomMemberCount(roomIds));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("createRoom error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<PageResult<RoomUserDTO>> getRoomMemberList(long roomId, String anchor, int limit) {
        if (limit > 50 || !anchorValid(anchor)) {
            return Response.fail(ExceptionCode.ERROR_PARAM);
        }
        if (StringUtils.isBlank(anchor)) {
            anchor = "0";
        }
        if (limit <= 0) {
            limit = 20;
        }
        try {
            return Response.success(roomManager.getRoomMemberList(roomId, anchor, limit));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("createRoom error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    private boolean anchorValid(String anchor) {
        if (StringUtils.isBlank(anchor)) {
            return true;
        }

        try {
            Integer.parseInt(anchor);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Response<Map<Long, Boolean>> isUserInRoom(long roomId, List<Long> uids) {
        if (CollectionUtils.isEmpty(uids)) {
            return Response.fail(ExceptionCode.ERROR_PARAM);
        }
        try {
            return Response.success(roomManager.isUserInRoom(roomId, uids));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("createRoom error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<RoomConfigDTO> getRoomConfig(long roomId) {
        try {
            return Response.success(roomManager.getRoomConfig(roomId));
        } catch (YppRunTimeException e) {
            return Response.fail(e.getCode().getCode(), e.getCode().getMessage());
        } catch (Exception e) {
            log.error("getRoomMemberCount error", e);
            return Response.fail(ExceptionCode.SERVER_ERROR);
        }
    }

    @Override
    public Response<PageResult<RoomDTO>> getOnlineRoomList(String anchor, int limit) {
        if (StringUtils.isBlank(anchor)) {
            anchor = "0";
        }

        if (limit <= 0) {
            limit = 20;
        }

        return Response.success(roomManager.getOnlineRoomList(anchor, limit));
    }
}
