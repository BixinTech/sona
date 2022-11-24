package cn.bixin.sona.server.room.mapper;

import cn.bixin.sona.server.room.domain.db.GroupMember;
import cn.bixin.sona.server.room.domain.db.GroupUserCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoomGroupMemberMapper {

    int insertSelective(GroupMember groupMember);

    GroupMember selectByRoomIdAndUid(@Param("roomId") long roomId, @Param("uid") long uid);

    int updateStatusLeave(@Param("roomId") long roomId, @Param("uid") long uid);

    /**
     * 解散房间成员
     *
     * @param roomId 房间
     * @return row
     */
    int disbandMember(@Param("roomId") long roomId);

    /**
     * 获取群组成员人数
     *
     * @param roomId: roomId
     * @return member count
     */
    int selectGroupMemberCountByRoomId(@Param("roomId") long roomId);

//    List<GroupMember> selectGroupMemberByRoomIds(@Param("roomIds") List<Long> roomIds);

    List<GroupUserCount> selectGroupUserCountByRoomIds(@Param("roomIds") List<Long> roomIds);

    List<GroupMember> selectGroupMemberListByRoomId(@Param("roomId") long roomId,
                                                          @Param("anchor") long anchor,
                                                          @Param("limit") int limit);

    List<GroupMember> selectByRoomIdAndUids(@Param("roomId") long roomId, @Param("uids") List<Long> uids);
}
