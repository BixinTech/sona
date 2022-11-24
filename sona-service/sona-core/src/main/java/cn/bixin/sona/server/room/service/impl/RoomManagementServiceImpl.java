package cn.bixin.sona.server.room.service.impl;

import cn.bixin.sona.server.room.domain.db.RoomManagementInfo;
import cn.bixin.sona.server.room.domain.db.RoomManagementLog;
import cn.bixin.sona.server.room.domain.enums.OperateEnum;
import cn.bixin.sona.server.room.domain.enums.RoomManagementEnum;
import cn.bixin.sona.server.room.domain.enums.UserRoleEnum;
import cn.bixin.sona.server.room.mapper.RoomManagementInfoMapper;
import cn.bixin.sona.server.room.mapper.RoomManagementLogMapper;
import cn.bixin.sona.server.room.service.RoomManagementService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class RoomManagementServiceImpl implements RoomManagementService {
    private static final Logger log = LoggerFactory.getLogger(RoomManagementServiceImpl.class);

    @Resource
    private RoomManagementInfoMapper roomManagementInfoMapper;
    @Resource
    private RoomManagementLogMapper roomManagementLogMapper;

    @Override
    public UserRoleEnum getUserRole(long roomId, long uid) {
        log.info("getUserRole roomId:{}, uid:{}", roomId, uid);

        List<RoomManagementInfo> infoList = roomManagementInfoMapper.getUserAllValidManagementInfo(roomId, String.valueOf(uid));
        if (CollectionUtils.isEmpty(infoList)) {
            return UserRoleEnum.MEMBER;
        }

        for (RoomManagementInfo info : infoList) {
            if (info.getType() == RoomManagementEnum.ADMIN.getCode()) {
                return UserRoleEnum.ADMIN;
            }

            if (info.getType() == RoomManagementEnum.OWNER.getCode()) {
                return UserRoleEnum.OWNER;
            }
        }

        return UserRoleEnum.MEMBER;
    }

    @Override
    public Map<Long, UserRoleEnum> getUserRoleBatch(long roomId, List<Long> uids) {
        log.info("getUserRoleBatch roomId:{}, uids:{}", roomId, uids);
        if (CollectionUtils.isEmpty(uids)) {
            return Collections.emptyMap();
        }

        List<RoomManagementInfo> userInfoList = roomManagementInfoMapper.getUserManagementInfoBatch(roomId, convertLongToStringList(uids));
        Map<Long, UserRoleEnum> roleMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(userInfoList)) {
            uids.forEach(uid -> roleMap.put(uid, UserRoleEnum.MEMBER));
        } else {
            userInfoList.forEach(userInfo -> roleMap.put(Long.valueOf(userInfo.getUid()), UserRoleEnum.getRoleByCode(userInfo.getType())));

            uids.forEach(uid -> roleMap.putIfAbsent(uid, UserRoleEnum.MEMBER));
        }

        return roleMap;
    }

    private List<String> convertLongToStringList(List<Long> longList) {
        if (CollectionUtils.isEmpty(longList)) {
            return Collections.emptyList();
        }

        List<String> list = Lists.newArrayList();
        longList.forEach(each -> list.add(String.valueOf(each)));

        return list;
    }

    @Override
    public boolean isUserMuted(long roomId, long uid) {
        log.info("isUserMuted roomId:{}, uid:{}", roomId, uid);
        RoomManagementInfo info = roomManagementInfoMapper.getUserManagementInfoByType(roomId, String.valueOf(uid), RoomManagementEnum.MUTE.getCode());
        if (info == null) {
            return false;
        }

        return !(info.getExpireTime() != null && System.currentTimeMillis() >= info.getExpireTime().getTime());
    }

    @Override
    public boolean isUserBlocked(long roomId, long uid) {
        log.info("isUserBlocked roomId:{}, uid:{}", roomId, uid);
        RoomManagementInfo info = roomManagementInfoMapper.getUserManagementInfoByType(roomId, String.valueOf(uid), RoomManagementEnum.BLOCK.getCode());
        return info != null;
    }

    @Override
    public boolean isUserAdmin(long roomId, long uid) {
        log.info("isUserAdmin roomId:{}, uid:{}", roomId, uid);
        RoomManagementInfo info = roomManagementInfoMapper.getUserManagementInfoByType(roomId, String.valueOf(uid), RoomManagementEnum.ADMIN.getCode());
        return info != null;
    }

    @Override
    public boolean isUserStreamPushForBid(long roomId, long uid) {
        log.info("isUserStreamPushForBid roomId:{}, uid:{}", roomId, uid);
        RoomManagementInfo info = roomManagementInfoMapper.getUserManagementInfoByType(roomId, String.valueOf(uid), RoomManagementEnum.STREAM_PUSH_FORBID.getCode());
        return info != null;
    }

    @Override
    public int addRoomManagementInfo(long roomId, long uid, int type, long operator) {
        log.info("addRoomManagementInfo roomId:{}, uid:{}, type:{}", roomId, uid, type);
        int ret = roomManagementInfoMapper.insertSelective(RoomManagementInfo.wrapNewObj(roomId, uid, type));
        if (ret > 0) {
            roomManagementLogMapper.insertSelective(RoomManagementLog.wrapRecordObj(roomId, uid, type, OperateEnum.SET.getCode(), operator));
        }
        return ret;
    }

    @Override
    public int updateManagementInfoInvalid(long roomId, long uid, int type, long operator) {
        log.info("updateManagementInfoInvalid roomId:{}, uid:{}, type:{}", roomId, uid, type);
        int ret = roomManagementInfoMapper.invalid(roomId, String.valueOf(uid), type);
        if (ret > 0) {
            roomManagementLogMapper.insertSelective(RoomManagementLog.wrapRecordObj(roomId, uid, type, OperateEnum.CANCEL.getCode(), operator));
        }
        return ret;
    }

    @Override
    public int muteTemporary(long roomId, long uid, int minute, long operator) {
        log.info("muteTemporary roomId:{}, uid:{}, minute:{}", roomId, uid, minute);
        int ret = roomManagementInfoMapper.insertSelective(RoomManagementInfo.wrapNewObj(roomId, uid, RoomManagementEnum.MUTE.getCode(), minute));
        if (ret > 0) {
            roomManagementLogMapper.insertSelective(RoomManagementLog.wrapRecordObj(roomId, uid, RoomManagementEnum.MUTE.getCode(), OperateEnum.SET.getCode(), operator));
        }
        return ret;
    }

    @Override
    public boolean isUserHasAdminAuth(long roomId, long uid) {
        log.info("isUserHasAuth roomId:{}, uid:{}", roomId, uid);
        return roomManagementInfoMapper.isUserAdminOrOwner(roomId, String.valueOf(uid)) > 0;
    }
}
