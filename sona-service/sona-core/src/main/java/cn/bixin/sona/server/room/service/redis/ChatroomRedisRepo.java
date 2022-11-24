package cn.bixin.sona.server.room.service.redis;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class ChatroomRedisRepo {

    private static final Logger log = LoggerFactory.getLogger(ChatroomRedisRepo.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    public static final int SMALL_RANGE_SIZE = 200;

    /**
     * 长连全量在线列表
     */
    private static final String SONA_KEY_CHATROOM_ALL_USER = "sona:all:u:set";

    public String getSonaChatroomAllUserKey(long roomId) {
        return Joiner.on(":").join(SONA_KEY_CHATROOM_ALL_USER, roomId);
    }

    /**
     * 小群组在线列表
     */
    private static final String SONA_KEY_CHATROOM_SMALL_RANGE_USER = "sona:c:u:zs";

    public String getSonaSmallRangeKey(long roomId) {
        return Joiner.on(":").join(SONA_KEY_CHATROOM_SMALL_RANGE_USER, roomId);
    }

    private String getSonaChatroomVipUserKey(long roomId, long uid) {
        return Joiner.on(":").join("vip:user:info:", roomId, uid);
    }

    /**
     * 活跃房间列表key
     */
    private static final String SONA_ACTIVE_ROOM_KEY_PREFIX = "sona:active:room";

    private String getActiveRoomSetRedisKey() {
        return Joiner.on(":").join(SONA_ACTIVE_ROOM_KEY_PREFIX, getPushForward5MinSeconds());
    }

    /**
     * sona:kick:out:user:flag
     *
     * @param roomId
     * @param targetUid
     * @return
     */
    private String getKickOutUserFlag(long roomId, String targetUid) {
        return Joiner.on(":").join("s:k:o:u:f", roomId, targetUid);
    }


    /**
     * 获取在线人数
     *
     * @param roomId
     * @return
     */
    public long getOnlineUserCount(long roomId) {
        Long count = redisTemplate.opsForSet().size(getSonaChatroomAllUserKey(roomId));
        return count == null ? 0L : count;
    }

    /**
     * 更新用户得分(长连在线列表)
     *
     * @param roomId 房间ID
     * @param uid    用户uid
     * @param score  用户得分
     */
    public void updateUserScore(long roomId, long uid, int score) {
        if (Boolean.FALSE.equals(redisTemplate.opsForSet().isMember(getSonaChatroomAllUserKey(roomId), String.valueOf(uid)))) {
            return;
        }
        String key = getSonaSmallRangeKey(roomId);
        redisTemplate.opsForZSet().add(key, String.valueOf(uid), score);
        redisTemplate.expire(key, 12, TimeUnit.HOURS);

        Long size = redisTemplate.opsForZSet().zCard(key);
        if (size != null && size > SMALL_RANGE_SIZE) {
            redisTemplate.opsForZSet().removeRange(key, 0, 0);
        }
    }

    public void leaveChatroom(long roomId, long uid) {
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        RedisCallback<String> callback = connection -> {
            connection.sRem(serializer.serialize(getSonaChatroomAllUserKey(roomId)), serializer.serialize(String.valueOf(uid)));
            connection.zRem(serializer.serialize(getSonaSmallRangeKey(roomId)), serializer.serialize(String.valueOf(uid)));
            connection.del(serializer.serialize(getSonaChatroomVipUserKey(roomId, uid)));
            return null;
        };
        redisTemplate.executePipelined(callback);
    }

    public void enterChatroom(long roomId, long uid) {
        redisTemplate.opsForSet().add(getSonaChatroomAllUserKey(roomId), uid + "");
        redisTemplate.expire(getSonaChatroomAllUserKey(roomId), 120, TimeUnit.MINUTES);
    }

    /**
     * 加入活跃房间列表
     *
     * @param roomId 房间ID
     */
    public void joinActiveRoomSet(long roomId) {
        if (activeRoomExists(roomId)) {
            return;
        }
        redisTemplate.opsForZSet().add(getActiveRoomSetRedisKey(), String.valueOf(roomId), System.currentTimeMillis());
        redisTemplate.expire(getActiveRoomSetRedisKey(), 20, TimeUnit.MINUTES);
    }

    /**
     * 设置用户被踢出房间标识
     *
     * @param roomId    房间ID
     * @param targetUid 目标用户
     */
    public void setKickOutUserFlag(long roomId, long targetUid) {
        String kickOutFlag = getKickOutUserFlag(roomId, String.valueOf(targetUid));
        //Set flag five minutes
        redisTemplate.opsForValue().set(kickOutFlag, String.valueOf(targetUid), 5, TimeUnit.MINUTES);
    }

    private boolean activeRoomExists(long roomId) {
        Long rank = redisTemplate.opsForZSet().rank(getActiveRoomSetRedisKey(), String.valueOf(roomId));
        return rank != null && (rank > 0);
    }

    /**
     * 获取当前时间往前推5分钟的一个整秒数
     *
     * <p>
     * 2021-10-11 15:55:30 -> 2021-10-11 15:55:00
     * 2021-10-11 15:51:10 -> 2021-10-11 15:50:00
     * </p>
     */
    private static long getPushForward5MinSeconds() {
        ZonedDateTime time = LocalDateTime.now().atZone(ZoneId.systemDefault());
        return time.minusMinutes(time.getMinute() % 5).minusSeconds(time.getSecond()).toLocalDateTime().toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * 分页获取聊天室在线人数列表
     *
     * @param roomId 房间ID
     * @param limit  条数
     * @return
     */
    public Set<String> getAllUserUidListByLimit(long roomId, int limit) {
        return redisTemplate.opsForSet().distinctRandomMembers(getSonaChatroomAllUserKey(roomId), limit);
    }

    /**
     * 用户是否被踢出了某个房间
     *
     * @param roomId    房间ID
     * @param targetUid 目标用户uid
     */
    public boolean hasKickOut(long roomId, String targetUid) {
        return redisTemplate.hasKey(getKickOutUserFlag(roomId, targetUid));
    }

    /**
     * 用户是否在在线列表中
     *
     * @param roomId 房间ID
     * @param uid    用户ID
     * @return TRUE/FALSE
     */
    public boolean userExistInOnlineList(long roomId, String uid) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(getSonaChatroomAllUserKey(roomId), uid));
        } catch (Exception e) {
            log.error("判断用户是否在在线列表中失败, roomId:[{}], uid:[{}]", roomId, uid, e);
            return false;
        }
    }

    public void enterChatroom(long roomId, String uid, int score) {
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        RedisCallback<String> callback = connection -> {
            byte[] allSet = serializer.serialize(getSonaChatroomAllUserKey(roomId));
            connection.sAdd(allSet, serializer.serialize(uid));
            connection.expire(allSet, 12 * 60 * 60);
            byte[] smallSet = serializer.serialize(getSonaSmallRangeKey(roomId));
            connection.zAdd(smallSet, score, serializer.serialize(uid));
            connection.expire(smallSet, 12 * 60 * 60);
            connection.zCard(smallSet);
            return null;
        };
        List<Object> results = redisTemplate.executePipelined(callback);
        Long size = (Long) results.get(results.size() - 1);
        if (size != null && size > SMALL_RANGE_SIZE) {
            redisTemplate.opsForZSet().removeRange(getSonaSmallRangeKey(roomId), 0, 0);
        }
    }

    public String getChatroomVipUser(long roomId, String uid) {
        return redisTemplate.opsForValue().get(getSonaChatroomVipUserKey(roomId, Long.parseLong(uid)));
    }

    public void saveUserVipInfo(long roomId, long uid) {
        redisTemplate.opsForValue().set(getSonaChatroomVipUserKey(roomId, uid), "0", 12, TimeUnit.HOURS);
    }

}
