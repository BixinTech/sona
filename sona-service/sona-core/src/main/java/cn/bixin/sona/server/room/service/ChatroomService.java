package cn.bixin.sona.server.room.service;

import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.enums.UserTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * 聊天室类型房间service
 */
public interface ChatroomService {

    boolean enterChatroom(long roomId, long uid, UserTypeEnum userType, Integer score);

    boolean leaveChatroom(long roomId, long uid);

    void removeChatroomUsers(long roomId);

    boolean kickoutChatroom(long roomId, long uid);

    boolean updateUserScore(long roomId, long uid, int score);

    long getChatroomUserCount(long roomId);

    Map<Long, Long> batchGetChatroomUserCount(List<Long> roomIds);

    PageResult<Long> getChatroomUserList(long roomId, String anchor, int limit);

    Map<Long, Boolean> isUserInChatroom(long roomId, List<Long> uids);
}
