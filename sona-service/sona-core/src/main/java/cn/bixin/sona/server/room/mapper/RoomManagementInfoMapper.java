package cn.bixin.sona.server.room.mapper;

import cn.bixin.sona.server.room.domain.db.RoomManagementInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoomManagementInfoMapper {

    int insertSelective(RoomManagementInfo record);

    /**
     * 使某个类型失效(单个用户)
     *
     * @param roomId: 房间Id
     * @param targetUid: 目标uid
     * @param  type: 类型
     */
    int invalid(@Param("roomId") long roomId, @Param("targetUid") String targetUid, @Param("type") int type);

    /**
     * 使某个类型失效(所有用户)
     *
     * @param roomId: 房间Id
     * @param type:   类型
     */
    int invalidType(@Param("roomId") long roomId, @Param("type") int type);

    /**
     * 使某个类型生效
     *
     * @param roomId:    房间Id
     * @param targetUid: 目标uid
     * @param type:      类型
     */
    int valid(@Param("roomId") long roomId, @Param("targetUid") String targetUid, @Param("type") int type);

    /**
     * 查询用户是否是管理员或者房主
     *
     * @param roomId:    房间Id
     * @param uid: 目标uid
     */
    Long isUserAdminOrOwner(@Param("roomId") long roomId, @Param("uid") String uid);

    /**
     * 查询用户管理信息
     *
     * @param roomId: roomId
     * @param uid: uid
     * @return management info
     */
    List<RoomManagementInfo> getUserAllValidManagementInfo(@Param("roomId") long roomId, @Param("uid") String uid);

    /**
     * 批量查询用户管理信息
     *
     * @param roomId: roomId
     * @param uids: uids
     * @return management info list
     */
    List<RoomManagementInfo> getUserManagementInfoBatch(@Param("roomId") long roomId, @Param("uidList") List<String> uids);

    /**
     * 根据用户type查询用户管理信息
     *
     * @param roomId: roomId
     * @param uid: uid
     * @param type:
     *
     * @return management info list
     */
    RoomManagementInfo getUserManagementInfoByType(@Param("roomId") long roomId, @Param("uid") String uid, @Param("type") int type);
}
