package cn.bixin.sona.server.room.service;

import cn.bixin.sona.common.dto.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 群组类型房间Service
 */
public interface GroupService {

    boolean enterGroup(long roomId, String productCode, long uid);

    boolean leaveGroup(long roomId, long uid);

    boolean removeGroupMembers(long roomId);

    long getGroupMemberCount(long roomId);

    Map<Long, Long> batchGetGroupMemberCount(List<Long> roomIds);

    PageResult<Long> getRoomMemberList(long roomId, String anchor, int limit);

    Map<Long, Boolean> isUserInGroup(long roomId, List<Long> uids);
}
