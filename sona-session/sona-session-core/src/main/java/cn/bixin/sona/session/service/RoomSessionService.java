package cn.bixin.sona.session.service;

import cn.bixin.sona.session.enums.LeaveReason;
import cn.bixin.sona.session.mq.RocketSender;
import cn.bixin.sona.session.utils.CacheKey;
import cn.bixin.sona.session.utils.Constants;
import com.alibaba.fastjson.JSONObject;
import com.dianping.cat.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author qinwei
 */
@Service
public class RoomSessionService {

    private static final Logger log = LoggerFactory.getLogger(RoomSessionService.class);

    private static final long REDIS_TTL_SESSION = 7 * 24 * 60 * 60;

    private static final String TOPIC_CHATROOM_SESSION_SONA = "TOPIC_CHATROOM_SESSION_SONA";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RocketSender rocketSender;

    @Resource
    private ServerStatCache serverStatCache;

    public void processChannelActive(JSONObject json) {
        String channelId = json.getString(Constants.MQ_REPORT_KEY_CHANNEL_ID);
        long timestamp = json.getLong(Constants.MQ_REPORT_KEY_TIMESTAMP_SHORT);
        log.info("channelActive, channelId={}, timestamp={}", channelId, timestamp);

        RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
        byte[] chKey = serializer.serialize(CacheKey.getSonaChannelKey(channelId));
        RedisCallback<String> callback = connection -> {
            connection.hSet(chKey, serializer.serialize(Constants.BODY_PARAM_DEVICEID), serializer.serialize(json.getString(Constants.BODY_PARAM_DEVICEID)));
            connection.hSet(chKey, serializer.serialize(Constants.MQ_REPORT_KEY_TIMESTAMP_SHORT), serializer.serialize(String.valueOf(timestamp)));
            String uid = json.getString(Constants.MQ_REPORT_KEY_UID);
            if (StringUtils.hasText(uid)) {
                connection.hSet(chKey, serializer.serialize(Constants.BODY_PARAM_UID), serializer.serialize(uid));
                connection.sAdd(serializer.serialize(CacheKey.getUidKey(uid)), serializer.serialize(channelId));
            }
            connection.expire(chKey, REDIS_TTL_SESSION);
            return null;
        };
        stringRedisTemplate.executePipelined(callback);
    }

    public void processChannelInactive(JSONObject json) {
        String channelId = json.getString(Constants.MQ_REPORT_KEY_CHANNEL_ID);
        String uid = json.getString(Constants.MQ_REPORT_KEY_UID);
        long timestamp = json.getLong(Constants.MQ_REPORT_KEY_TIMESTAMP_SHORT);
        log.info("channelIctive, channelId={}, uid={}, timestamp={}", channelId, uid, timestamp);

        Map<String, String> channelRoomsToLeave = new HashMap<>();
        String uKey = CacheKey.getSonaUidKey(uid);
        if (StringUtils.hasText(uid)) {
            HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
            Map<String, String> allChannelRooms = opsForHash.entries(uKey);
            allChannelRooms.entrySet().stream().filter(entry -> entry.getKey().equals(channelId)).forEach(entry -> channelRoomsToLeave.put(entry.getKey(), entry.getValue()));
            Map<String, LeaveReason> roomsAffected = compareWhenLeaveRoom(uid, allChannelRooms, channelRoomsToLeave, LeaveReason.CHANNEL_CLOSE);
            if (!CollectionUtils.isEmpty(roomsAffected)) {
                log.info("leave room completely, reportSona, channelId={}, uid={}, roomsAffected={}", channelId, uid, roomsAffected);
                reportSona(timestamp, 11, roomsAffected, uid);
            }
        }

        RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
        RedisCallback<List<Object>> callback = connection -> {
            if (!CollectionUtils.isEmpty(channelRoomsToLeave)) {
                for (String ch : channelRoomsToLeave.keySet()) {
                    connection.hDel(serializer.serialize(uKey), serializer.serialize(ch));
                }
            }
            connection.sRem(serializer.serialize(CacheKey.getUidKey(uid)), serializer.serialize(channelId));
            connection.del(serializer.serialize(CacheKey.getSonaChannelKey(channelId)));
            return null;
        };
        stringRedisTemplate.executePipelined(callback);
    }

    public void processEnterRoom(JSONObject json) {
        String channelId = json.getString(Constants.MQ_REPORT_KEY_CHANNEL_ID);
        Long timestamp = json.getLong(Constants.MQ_REPORT_KEY_TIMESTAMP_SHORT);
        String room = json.getString(Constants.MQ_REPORT_KEY_ROOM);
        String uid = json.getString(Constants.MQ_REPORT_KEY_UID);
        Integer cmd = json.getInteger(Constants.MQ_REPORT_KEY_CMD);
        log.info("channel enter room, channelId={}, uid={}, room={}, timestamp={}", channelId, uid, room, timestamp);

        reportSona(timestamp, cmd, room, uid, null);

        RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
        byte[] chKey = serializer.serialize(CacheKey.getSonaChannelKey(channelId));
        byte[] uKey = serializer.serialize(CacheKey.getSonaUidKey(uid));
        RedisCallback<List<Object>> callback = connection -> {
            connection.hSet(chKey, serializer.serialize(Constants.BODY_PARAM_UID), serializer.serialize(uid));
            connection.hSet(uKey, serializer.serialize(channelId), serializer.serialize(room));
            connection.expire(chKey, REDIS_TTL_SESSION);
            connection.expire(uKey, REDIS_TTL_SESSION);
            return null;
        };
        stringRedisTemplate.executePipelined(callback);
    }

    public void processLeaveRoom(JSONObject json) {
        Long timestamp = json.getLong(Constants.MQ_REPORT_KEY_TIMESTAMP_SHORT);
        String room = json.getString(Constants.MQ_REPORT_KEY_ROOM);
        String uid = json.getString(Constants.MQ_REPORT_KEY_UID);
        Integer cmd = json.getInteger(Constants.MQ_REPORT_KEY_CMD);
        String channelId = json.getString(Constants.MQ_REPORT_KEY_CHANNEL_ID);

        String uKey = CacheKey.getSonaUidKey(uid);
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        Map<String, String> allChannelRooms = opsForHash.entries(uKey);
        if (!allChannelRooms.containsValue(room)) {
            log.warn("NotInRoomWhenLeave, channelId={}, uid={}, allChannelRooms={}, room={}", channelId, uid, allChannelRooms, room);
            Cat.logEvent(Constants.CHATROOM_SESSION_PROBLEM, "NotInRoomWhenLeave");
        }

        Map<String, LeaveReason> roomsAffected = compareWhenLeaveRoom(uid, allChannelRooms, Collections.singletonMap(channelId, room), LeaveReason.BUSINESS);
        // 如果是被后端sona强制踢出，则不需要重新上报sona
        if (cmd == 13) {
            if (roomsAffected.remove(room) != null) {
                log.info("kicked out of room, channelId={}, uid={}, room={}", channelId, uid, room);
            }
        }
        if (!CollectionUtils.isEmpty(roomsAffected)) {
            log.info("leave room completely, reportSona, channelId={}, uid={}, roomsAffected={}", channelId, uid, roomsAffected);
            reportSona(timestamp, 11, roomsAffected, uid);
        }

        opsForHash.delete(uKey, channelId);
        log.info("leaveRoom, channelId={}, uid={}, room={}, timestamp={}", channelId, uid, room, timestamp);
    }

    /**
     * 比较用户所有的<连接,房间>，判断当前想要离开的<连接,房间>是否最后一个离开房间的连接。
     * 同时还会根据server的心跳信息，找出过期无效的连接，同时进行清除。
     *
     * @param uid
     * @param allChannelRooms     用户当前所有的<连接,房间>
     * @param channelRoomsToLeave 当前想要离开房间的<连接,房间>
     * @return 实际彻底离开的房间
     */
    private Map<String, LeaveReason> compareWhenLeaveRoom(String uid, Map<String, String> allChannelRooms, Map<String, String> channelRoomsToLeave, LeaveReason leaveReason) {
        Set<String> roomsRemaining = new HashSet<>();
        Map<String, LeaveReason> roomsAffected = new HashMap<>();
        for (Map.Entry<String, String> entry : allChannelRooms.entrySet()) {
            String channelId = entry.getKey();
            String room = entry.getValue();
            boolean channelValid = serverStatCache.judgeChannelValid(channelId);
            if (!channelValid) {
                deleteInvalidChannel(uid, channelId, room);
                roomsAffected.putIfAbsent(room, LeaveReason.CHANNEL_INVALID);
            }
            boolean shouldLeave = channelRoomsToLeave.containsValue(room);
            if (shouldLeave) {
                roomsAffected.put(room, leaveReason);
            } else {
                roomsRemaining.add(room);
            }
        }
        roomsAffected.entrySet().removeIf(entry -> roomsRemaining.contains(entry.getKey()));
        return roomsAffected;
    }

    private void deleteInvalidChannel(String uid, String channelId, String room) {
        RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
        RedisCallback<List<Object>> callback = connection -> {
            connection.hDel(serializer.serialize(CacheKey.getSonaUidKey(uid)), serializer.serialize(channelId));
            connection.del(serializer.serialize(CacheKey.getSonaChannelKey(channelId)));
            return null;
        };
        stringRedisTemplate.executePipelined(callback);
        log.info("deleteInvalidChannel, uid={}, channelId={}, room={}", uid, channelId, room);
    }

    private void reportSona(long timestamp, int cmd, Map<String, LeaveReason> rooms, String uid) {
        for (Map.Entry<String, LeaveReason> entry : rooms.entrySet()) {
            Cat.logEvent(Constants.LEAVE_REASON, entry.getValue().toString());
            reportSona(timestamp, cmd, entry.getKey(), uid, entry.getValue());
        }
    }

    private void reportSona(long timestamp, int cmd, String room, String uid, LeaveReason leaveReason) {
        JSONObject jsonParam = new JSONObject();
        jsonParam.put(Constants.MQ_REPORT_KEY_TIMESTAMP, timestamp);
        jsonParam.put(Constants.MQ_REPORT_KEY_ROOM, room);
        jsonParam.put(Constants.MQ_REPORT_KEY_UID, uid);
        jsonParam.put(Constants.MQ_REPORT_KEY_CMD, cmd);
        if (leaveReason != null) {
            jsonParam.put(Constants.LEAVE_REASON, leaveReason.getCode());
        }
        rocketSender.syncSend(TOPIC_CHATROOM_SESSION_SONA, null, uid, jsonParam.toJSONString());
    }

}
