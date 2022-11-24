package cn.bixin.sona.server.room.manager;

import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.common.enums.RoomStatus;
import cn.bixin.sona.common.exception.YppRunTimeException;
import cn.bixin.sona.common.util.IdGenerator;
import cn.bixin.sona.dto.*;
import cn.bixin.sona.enums.UserTypeEnum;
import cn.bixin.sona.request.*;
import cn.bixin.sona.server.exception.ExceptionCode;
import cn.bixin.sona.server.room.domain.convert.RoomDTOConverter;
import cn.bixin.sona.server.room.domain.convert.RoomDetailInfoConverter;
import cn.bixin.sona.server.room.domain.db.ProductConfig;
import cn.bixin.sona.server.room.domain.db.Room;
import cn.bixin.sona.server.room.domain.db.RoomConfig;
import cn.bixin.sona.server.room.domain.db.RoomOperateLog;
import cn.bixin.sona.server.room.domain.enums.*;
import cn.bixin.sona.server.room.mapper.RoomOperateLogMapper;
import cn.bixin.sona.server.room.service.*;
import com.alibaba.fastjson.JSON;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.bixin.sona.server.room.domain.enums.TransactionTypeEnum.*;

/**
 * 聊天室类型房间
 */
@Service
public class RoomManagerImpl implements RoomManager {

    private static final Logger log = LoggerFactory.getLogger(RoomManagerImpl.class);

    @Resource
    private IdGenerator idGenerator;
    @Resource
    private StreamManager streamManager;

    @Resource
    private RoomService roomService;
    @Resource
    private ChatroomService chatroomService;
    @Resource
    private GroupService groupService;
    @Resource
    private ProductConfigService productConfigService;
    @Resource
    private RoomManagementService roomManagementService;
    @Resource
    private MessageService messageService;

    @Resource
    private RoomOperateLogMapper roomOperateLogMapper;

    @Override
    public RoomDTO getRoom(long roomId) {
        return roomService.getRoomByRoomId(roomId);
    }

    @Override
    public Map<Long, RoomDTO> getRoomBatch(List<Long> roomIds) {
        return roomService.batchGetRooms(roomIds);
    }

    @Override
    public long getRoomMemberCount(long roomId) {
        RoomDTO room = roomService.getRoomByRoomId(roomId);
        checkRoomValid(room);
        ProductConfig config = productConfigService.getConfigInfoByCode(room.getProductCode());
        if (IMModuleEnum.CHATROOM.name().equals(config.getImModule())) {
            return chatroomService.getChatroomUserCount(roomId);
        } else {
            return groupService.getGroupMemberCount(roomId);
        }
    }

    @Override
    public Map<Long, Long> batchGetRoomMemberCount(List<Long> roomIds) {
        RoomDTO room = roomService.getRoomByRoomId(roomIds.get(0));
        checkRoomValid(room);
        ProductConfig config = productConfigService.getConfigInfoByCode(room.getProductCode());
        if (IMModuleEnum.CHATROOM.name().equals(config.getImModule())) {
            return chatroomService.batchGetChatroomUserCount(roomIds);
        } else {
            return groupService.batchGetGroupMemberCount(roomIds);
        }
    }

    @Override
    public PageResult<RoomUserDTO> getRoomMemberList(long roomId, String anchor, int limit) {
        RoomDTO room = roomService.getRoomByRoomId(roomId);
        checkRoomValid(room);
        ProductConfig config = productConfigService.getConfigInfoByCode(room.getProductCode());
        PageResult<Long> uidPage;
        if (IMModuleEnum.CHATROOM.name().equals(config.getImModule())) {
            uidPage = chatroomService.getChatroomUserList(roomId, anchor, limit);
        } else {
            uidPage = groupService.getRoomMemberList(roomId, anchor, limit);
        }
        if (CollectionUtils.isEmpty(uidPage.getList())) {
            return PageResult.newPageResult(Collections.emptyList(), true);
        }
        List<Long> uids = uidPage.getList();
        Map<Long, UserRoleEnum> memberRoleMap = roomManagementService.getUserRoleBatch(roomId, uids);
        return PageResult.newPageResult(
                uids.stream().map(uid -> genRoomUserDTO(uid, memberRoleMap)).collect(Collectors.toList()),
                uidPage.getEnd(), uidPage.getCount(), uidPage.getAnchor()
        );
    }

    private RoomUserDTO genRoomUserDTO(long uid, Map<Long, UserRoleEnum> memberRoleMap) {
        RoomUserDTO roomUserDTO = new RoomUserDTO();
        roomUserDTO.setUid(uid);
        UserRoleEnum userRoleEnum = memberRoleMap.get(uid);
        if (userRoleEnum != null) {
            roomUserDTO.setRole(userRoleEnum.getCode());
        }
        return roomUserDTO;
    }

    @Override
    public Map<Long, Boolean> isUserInRoom(long roomId, List<Long> uids) {
        RoomDTO room = roomService.getRoomByRoomId(roomId);
        checkRoomValid(room);
        ProductConfig config = productConfigService.getConfigInfoByCode(room.getProductCode());
        if (IMModuleEnum.CHATROOM.name().equals(config.getImModule())) {
            return chatroomService.isUserInChatroom(roomId, uids);
        } else {
            return groupService.isUserInGroup(roomId, uids);
        }
    }

    @Override
    public RoomDetailInfoDTO createRoom(CreateRoomRequest request) {
        ProductConfig config = productConfigService.getConfigInfoByCode(request.getProductCode());
        if (config == null) {
            throw new YppRunTimeException(ExceptionCode.PRODUCT_NOT_FOUND);
        }
        String data = request.getProductCode() + "_" + request.getUid();
        Transaction transaction = Cat.newTransaction(TransactionTypeEnum.TR_ROOM_CREATE.getCode(), TransactionTypeEnum.TR_ROOM_CREATE.name());
        try {
            log.debug("createRoom request:{}", JSON.toJSONString(request));
            Room room = roomService.createRoom(request, config);

            roomManagementService.addRoomManagementInfo(room.getRoomId(), request.getUid(), RoomManagementEnum.OWNER.getCode(), request.getUid());
            logRoomOperateRecord(RoomOperateLog.wrapRoomLog(room.getRoomId(), config.getImModule(), RoomOperateEnum.CREATE.name(), String.valueOf(request.getUid())));

            transaction.addData("data", data);
            transaction.setSuccessStatus();
            return wrapRoomDetailInfoDTO(config, RoomDTOConverter.convertDO(room), room.getUid());
        } catch (Exception e) {
            log.error("createRoom manager error", e);
            transaction.setStatus(e);
            throw new YppRunTimeException(ExceptionCode.CREATE_CHATROOM_ERROR);
        } finally {
            transaction.complete();
        }
    }

    @Override
    public RoomDetailInfoDTO openRoom(OpenCloseRoomRequest request) {
        RoomDTO room = roomService.getRoomByRoomId(request.getRoomId());
        if (room == null) {
            throw new YppRunTimeException(ExceptionCode.CHATROOM_NOT_EXISTS);
        }
        checkUserHasAdminAuth(request.getRoomId(), request.getUid());

        String data = room.getRoomId() + "_" + room.getUid();
        Transaction transaction = Cat.newTransaction(TR_ROOM_OPEN.getCode(), TR_ROOM_OPEN.name());
        try {
            ProductConfig config = productConfigService.getConfigInfoByCode(room.getProductCode());
            roomService.openRoom(request);
            messageService.sendSonaCreateRoomMessage(request.getRoomId(), request.getUid(), config.getImModule());

            logRoomOperateRecord(RoomOperateLog.wrapRoomLog(room.getRoomId(), config.getImModule(), RoomOperateEnum.OPEN.name(), String.valueOf(request.getUid())));
            transaction.addData("data", data);
            transaction.setSuccessStatus();
            return wrapRoomDetailInfoDTO(config, room, room.getUid());
        } catch (Exception e) {
            transaction.setStatus(e);
            log.error("openRoom manager error", e);
            throw new YppRunTimeException(ExceptionCode.OPEN_CLOSE_CHATROOM_ERROR);
        } finally {
            transaction.complete();
        }
    }

    @Override
    public boolean closeRoom(OpenCloseRoomRequest request) {
        RoomDTO room = roomService.getRoomByRoomId(request.getRoomId());
        checkRoomValid(room);
        checkUserHasAdminAuth(request.getRoomId(), request.getUid());

        String data = room.getRoomId() + "_" + room.getUid();
        Transaction transaction = Cat.newTransaction(TR_ROOM_CLOSE.getCode(), TR_ROOM_CLOSE.name());
        try {
            ProductConfig config = productConfigService.getConfigInfoByCode(room.getProductCode());
            boolean ret = roomService.closeRoom(request);
            logRoomOperateRecord(RoomOperateLog.wrapRoomLog(room.getRoomId(), config.getImModule(), RoomOperateEnum.CLOSE.name(), String.valueOf(request.getUid())));
            if (IMModuleEnum.CHATROOM.name().equals(config.getImModule())) {
                chatroomService.removeChatroomUsers(room.getRoomId());
            } else {
                groupService.removeGroupMembers(room.getRoomId());
            }
            messageService.sendSonaCloseRoomMessage(request.getRoomId(), request.getUid(), config.getImModule());
            transaction.addData("data", data);
            transaction.setSuccessStatus();
            return ret;
        } catch (Exception e) {
            transaction.setStatus(e);
            log.error("openRoom manager error", e);
            throw new YppRunTimeException(ExceptionCode.OPEN_CLOSE_CHATROOM_ERROR);
        } finally {
            transaction.complete();
        }
    }

    @Override
    public RoomDetailInfoDTO enterRoom(EnterRoomRequest request) {
        RoomDTO room = roomService.getRoomByRoomId(request.getRoomId());
        checkRoomValid(room);
        if (StringUtils.isNotBlank(room.getPassword()) && !StringUtils.equals(room.getPassword(), request.getPassword())) {
            throw new YppRunTimeException(ExceptionCode.ERROR_PASSWORD);
        }
        if (request.getUid() != 0) {
            boolean isBlocked = roomManagementService.isUserBlocked(request.getRoomId(), request.getUid());
            if (isBlocked) {
                throw new YppRunTimeException(ExceptionCode.HAS_BEEN_BLOCKED);
            }
        }
        ProductConfig config = productConfigService.getConfigInfoByCode(room.getProductCode());
        if (IMModuleEnum.CHATROOM.name().equals(config.getImModule())) {
            if (request.getUid() == 0) {
                return guestEnterChatroom(room, config);
            } else {
                return userEnterChatroom(room, config, request.getUid(), request.getUserTypeEnum());
            }
        } else {
            if (request.getUid() == 0) {
                throw new YppRunTimeException(ExceptionCode.EMPTY_PARAM);
            }
            return enterGroup(room, config, request.getUid());
        }
    }

    @Override
    public boolean leaveRoom(LeaveRoomRequest request) {
        if (request.getUid() == 0){
            return true;
        }
        RoomDTO room = roomService.getRoomByRoomId(request.getRoomId());
        checkRoomValid(room);
        ProductConfig config = productConfigService.getConfigInfoByCode(room.getProductCode());
        boolean ret;
        if (IMModuleEnum.CHATROOM.name().equals(config.getImModule())) {
            ret = chatroomService.leaveChatroom(request.getRoomId(), request.getUid());
        } else {
            if (room.getUid() == request.getUid()) {
                return false;
            }
            ret = groupService.leaveGroup(request.getRoomId(), request.getUid());
        }
        if (ret && config.isEnterNotifySwitch()) {
            messageService.sendSonaLeaveRoomMessage(request.getRoomId(), request.getUid(), config.getImModule());
        }
        return ret;
    }

    @Override
    public boolean updatePassword(UpdatePasswordRequest request) {
        if (request == null || request.getRoomId() == 0 || request.getOperatorUid() == 0) {
            throw new YppRunTimeException(ExceptionCode.EMPTY_PARAM);
        }
        RoomDTO room = roomService.getRoomByRoomId(request.getRoomId());
        checkRoomValid(room);
        checkUserHasAdminAuth(request.getRoomId(), request.getOperatorUid());
        if (StringUtils.isNotBlank(room.getPassword()) && !room.getPassword().equals(request.getOldPassword())) {
            throw new YppRunTimeException(ExceptionCode.ERROR_PASSWORD);
        }
        boolean ret = roomService.updatePassword(request.getRoomId(), request.getNewPassword());
        if (ret) {
            ProductConfig config = productConfigService.getConfigInfoByCode(room.getProductCode());
            logRoomOperateRecord(RoomOperateLog.wrapRoomLog(room.getRoomId(), config.getImModule(), RoomOperateEnum.UPDATE_PASSWORD.name(), String.valueOf(request.getOperatorUid())));
        }
        return ret;
    }

    @Override
    public boolean muteOrCancelUser(OperateRequest request) {
        RoomDTO room = roomService.getRoomByRoomId(request.getRoomId());
        checkRoomValid(room);
        checkUserOperateAuth(request.getRoomId(), request.getOperatorUid(), request.getTargetUid());
        boolean isMuted = roomManagementService.isUserMuted(request.getRoomId(), request.getTargetUid());
        if (request.getOperate() == OperateEnum.SET.getCode()) {
            if (isMuted) {
                return true;
            }
            roomManagementService.muteTemporary(request.getRoomId(), request.getTargetUid(), request.getMinutes(), request.getOperatorUid());
        } else {
            if (!isMuted) {
                return true;
            }
            roomManagementService.updateManagementInfoInvalid(request.getRoomId(), request.getTargetUid(), RoomManagementEnum.MUTE.getCode(), request.getOperatorUid());
        }

        ProductConfig config = productConfigService.getConfigInfoByCode(room.getProductCode());
        messageService.sendSonaMuteMessage(request.getRoomId(), request.getTargetUid(), request.getOperate() == OperateEnum.SET.getCode(), request.getMinutes(), config.getImModule());
        return true;
    }

    @Override
    public boolean blockOrCancelUser(OperateRequest request) {
        RoomDTO room = roomService.getRoomByRoomId(request.getRoomId());
        checkRoomValid(room);
        checkUserOperateAuth(request.getRoomId(), request.getOperatorUid(), request.getTargetUid());

        ProductConfig config = productConfigService.getConfigInfoByCode(room.getProductCode());
        boolean isBlocked = roomManagementService.isUserBlocked(request.getRoomId(), request.getTargetUid());
        if (request.getOperate() == OperateEnum.SET.getCode()) {
            if (isBlocked) {
                return true;
            }
            roomManagementService.addRoomManagementInfo(request.getRoomId(), request.getTargetUid(), RoomManagementEnum.BLOCK.getCode(), request.getOperatorUid());

            if (IMModuleEnum.CHATROOM.name().equals(config.getImModule())) {
                chatroomService.kickoutChatroom(request.getRoomId(), request.getTargetUid());
            } else {
                groupService.leaveGroup(request.getRoomId(), request.getTargetUid());
            }
        } else {
            if (!isBlocked) {
                return true;
            }
            roomManagementService.updateManagementInfoInvalid(request.getRoomId(), request.getTargetUid(), RoomManagementEnum.BLOCK.getCode(), request.getOperatorUid());
        }

        messageService.sendSonaBlockMessage(request.getRoomId(), request.getTargetUid(), request.getOperate() == OperateEnum.SET.getCode(), config.getImModule());
        return true;
    }

    @Override
    public boolean kickUser(OperateRequest request) {
        RoomDTO room = roomService.getRoomByRoomId(request.getRoomId());
        checkRoomValid(room);
        checkUserOperateAuth(request.getRoomId(), request.getOperatorUid(), request.getTargetUid());

        ProductConfig config = productConfigService.getConfigInfoByCode(room.getProductCode());

        if (IMModuleEnum.CHATROOM.name().equals(config.getImModule())) {
            chatroomService.kickoutChatroom(request.getRoomId(), request.getTargetUid());
            //todo 踢人消息？
//            ChatroomMsgDTO chatroomMsgDTO = new ChatroomMsgDTO();
//            chatroomMsgDTO.setRoomId(String.valueOf(request.getRoomId()));
//            chatroomMsgDTO.setContent("踢人");
//            roomMessageRemoteService.kickOutChatroom(chatroomMsgDTO, request.getTargetUid());
        } else {
            groupService.leaveGroup(request.getRoomId(), request.getTargetUid());
        }

        messageService.sendSonaKickMessage(request.getRoomId(), request.getTargetUid(), config.getImModule());
        return true;
    }

    @Override
    public boolean setOrRemoveAdmin(OperateRequest request) {
        RoomDTO room = roomService.getRoomByRoomId(request.getRoomId());
        checkRoomValid(room);
        checkUserOperateAuth(request.getRoomId(), request.getOperatorUid(), request.getTargetUid());
        boolean isAdmin = roomManagementService.isUserAdmin(request.getRoomId(), request.getTargetUid());
        if (request.getOperate() == OperateEnum.SET.getCode()) {
            if (isAdmin) {
                return true;
            }
            roomManagementService.addRoomManagementInfo(request.getRoomId(), request.getTargetUid(), RoomManagementEnum.ADMIN.getCode(), request.getOperatorUid());
        } else {
            if (!isAdmin) {
                return true;
            }
            roomManagementService.updateManagementInfoInvalid(request.getRoomId(), request.getTargetUid(), RoomManagementEnum.ADMIN.getCode(), request.getOperatorUid());
        }

        ProductConfig config = productConfigService.getConfigInfoByCode(room.getProductCode());
        messageService.sendSonaAdminMessage(request.getRoomId(), request.getTargetUid(), request.getOperate() == OperateEnum.SET.getCode(), config.getImModule());
        return true;
    }

    @Override
    public boolean updateChatroomUserScore(long roomId, long uid, int score) {
        RoomDTO room = roomService.getRoomByRoomId(roomId);
        checkRoomValid(room);
        ProductConfig config = productConfigService.getConfigInfoByCode(room.getProductCode());
        if (IMModuleEnum.GROUP.name().equals(config.getImModule())) {
            return false;
        }
        return chatroomService.updateUserScore(roomId, uid, score);
    }

    @Override
    public RoomConfigDTO getRoomConfig(long roomId) {
        RoomDTO room = roomService.getRoomByRoomId(roomId);
        checkRoomValid(room);

        RoomConfigDTO result = new RoomConfigDTO();
        RoomConfig roomConfig = productConfigService.getRoomConfig(roomId);
        if (roomConfig != null) {
            BeanUtils.copyProperties(roomConfig, result);
            return result;
        }

        ProductConfig productConfig = productConfigService.getConfigInfoByCode(room.getProductCode());
        result.setRoomId(roomId);
        BeanUtils.copyProperties(productConfig, result);
        return result;
    }

    @Override
    public PageResult<RoomDTO> getOnlineRoomList(String anchor, int limit) {
        if (StringUtils.isBlank(anchor)) {
            anchor = "0";
        }

        if (limit <= 0) {
            limit = 20;
        }

        return roomService.getOnlineRoomList(anchor, limit);
    }

    private void checkRoomValid(RoomDTO room) {
        if (room == null) {
            throw new YppRunTimeException(ExceptionCode.CHATROOM_NOT_EXISTS);
        }
        if (room.getStatus() == RoomStatus.INVALID.getCode()) {
            throw new YppRunTimeException(ExceptionCode.ROOM_CLOSED_ERROR);
        }
    }

    private void checkUserHasAdminAuth(long roomId, long uid) {
        if (!roomManagementService.isUserHasAdminAuth(roomId, uid)) {
            throw new YppRunTimeException(ExceptionCode.ERROR_AUTH);
        }
    }

    private void checkUserOperateAuth(long roomId, long operatorUid, long targetUid) {
        log.info("checkUserOperateAuth roomId:{}, operatorUid:{}, targetUid:{}", roomId, operatorUid, targetUid);
        List<Long> uids = Lists.newArrayList(operatorUid, targetUid);
        Map<Long, UserRoleEnum> userRoleMap = roomManagementService.getUserRoleBatch(roomId, uids);
        UserRoleEnum operatorRole = userRoleMap.get(operatorUid);
        UserRoleEnum targetRole = userRoleMap.get(targetUid);

        if (hasNoAuth(operatorRole, targetRole)) {
            throw new YppRunTimeException(ExceptionCode.ERROR_AUTH);
        }
    }

    private boolean hasNoAuth(UserRoleEnum operatorRole, UserRoleEnum targetRole) {
        if (operatorRole.equals(targetRole) && operatorRole == UserRoleEnum.OWNER) {
            return false;
        }else {
            return targetRole == UserRoleEnum.OWNER ||
                    targetRole == UserRoleEnum.ADMIN && (operatorRole == UserRoleEnum.MEMBER || operatorRole == UserRoleEnum.ADMIN) ||
                    targetRole == UserRoleEnum.MEMBER && operatorRole == UserRoleEnum.MEMBER;
        }
    }

    private RoomDetailInfoDTO enterGroup(RoomDTO room, ProductConfig config, long uid) {
        boolean ret = groupService.enterGroup(room.getRoomId(), config.getProductCode(), uid);
        if (ret && config.isEnterNotifySwitch()) {
            messageService.sendSonaEnterRoomMessage(room.getRoomId(), uid, config.getImModule());
        }
        return wrapRoomDetailInfoDTO(config, room, uid);
    }

    private RoomDetailInfoDTO guestEnterChatroom(RoomDTO room, ProductConfig config) {
        long guestUid = idGenerator.id();
        return wrapRoomDetailInfoDTOForChatroomGuest(config, room, guestUid);
    }

    private RoomDetailInfoDTO userEnterChatroom(RoomDTO room, ProductConfig config, long uid, UserTypeEnum userTypeEnum) {
        // 可扩展score
        boolean ret = chatroomService.enterChatroom(room.getRoomId(), uid, userTypeEnum, userTypeEnum == UserTypeEnum.VIP ? 100: 1);
        if (ret && config.isEnterNotifySwitch()) {
            messageService.sendSonaEnterRoomMessage(room.getRoomId(), uid, config.getImModule());
        }
        return wrapRoomDetailInfoDTO(config, room, uid);
    }

    private void logRoomOperateRecord(RoomOperateLog info) {
        roomOperateLogMapper.insertSelective(info);
    }

    private RoomDetailInfoDTO wrapRoomDetailInfoDTO(ProductConfig config, RoomDTO room, long uid) {
        return RoomDetailInfoConverter.convertDetailObj(config, room, wrapStreamConfigInfo(config, room, uid));
    }

    private StreamConfigInfoDTO wrapStreamConfigInfo(ProductConfig config, RoomDTO room, long uid) {
        return streamManager.createStreamConfig(config, room, uid, false);
    }

    private RoomDetailInfoDTO wrapRoomDetailInfoDTOForChatroomGuest(ProductConfig config, RoomDTO room, long guestUid) {
        return RoomDetailInfoConverter.convertDetailObjGuest(config, room, wrapStreamConfigInfoForChatroomGuest(config, room, guestUid), guestUid);
    }

    protected StreamConfigInfoDTO wrapStreamConfigInfoForChatroomGuest(ProductConfig config, RoomDTO room, long uid) {
        return streamManager.createStreamConfig(config, room, uid,true);
    }
}
