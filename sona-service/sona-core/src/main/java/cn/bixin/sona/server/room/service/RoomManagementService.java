package cn.bixin.sona.server.room.service;

import cn.bixin.sona.server.room.domain.enums.UserRoleEnum;

import java.util.List;
import java.util.Map;

public interface RoomManagementService {

    /**
     * 查询用户角色
     *
     * @param roomId: roomId
     * @param uid: uid
     * @return: user role
     */
    UserRoleEnum getUserRole(long roomId, long uid);

    /**
     * 批量查询用户角色
     *
     * @param roomId: roomId
     * @param uids: uids
     * @return: user role map
     */
    Map<Long, UserRoleEnum> getUserRoleBatch(long roomId, List<Long> uids);

    /**
     * 添加管理信息
     *
     * @param roomId: roomId
     * @param uid: uid
     * @param type: 1拉黑、2禁言、3踢出 4、管理员 5、房主
     */
    int addRoomManagementInfo(long roomId, long uid, int type, long operator);

    /**
     * 将信息设置为无效
     *
     * @param roomId: roomId
     * @param uid: uid
     * @param type: 1拉黑、2禁言、3踢出 4、管理员 5、房主
     */
    int updateManagementInfoInvalid(long roomId, long uid, int type, long operator);

    /**
     * 查询用户是否有管理员权限
     *
     * @param roomId: roomId
     * @param uid: 用户uid
     * @return true has auth, otherwise false
     */
    boolean isUserHasAdminAuth(long roomId, long uid);

    /**
     * 查询用户是否被禁言
     *
     * @param roomId: roomId
     * @param uid: 用户uid
     * @return true has been muted, otherwise false
     */
    boolean isUserMuted(long roomId, long uid);

    /**
     * 查询用户是否被踢出
     *
     * @param roomId: roomId
     * @param uid: 用户uid
     * @return true has been blocked, otherwise false
     */
    boolean isUserBlocked(long roomId, long uid);

    /**
     * 临时禁言
     *
     * @param roomId 房间
     * @param uid    用户
     * @param minute 分钟
     * @return
     */
    int muteTemporary(long roomId, long uid, int minute, long operator);

    /**
     * 查询用户是否是管理员
     *
     * @param roomId: roomId
     * @param uid: 用户uid
     * @return true user is admin, otherwise false
     */
    boolean isUserAdmin(long roomId, long uid);

    /**
     * 查询用户是否被禁止推流
     * @param roomId
     * @param uid
     * @return
     */
    boolean isUserStreamPushForBid(long roomId, long uid);
}
