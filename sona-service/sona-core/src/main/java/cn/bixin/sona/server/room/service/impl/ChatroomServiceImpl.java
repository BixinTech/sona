package cn.bixin.sona.server.room.service.impl;

import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.enums.UserTypeEnum;
import cn.bixin.sona.server.room.service.ChatroomService;
import cn.bixin.sona.server.room.service.redis.ChatroomRedisRepo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChatroomServiceImpl implements ChatroomService {

    private static final Logger log = LoggerFactory.getLogger(ChatroomServiceImpl.class);

    @Resource
    private ChatroomRedisRepo chatroomRedisRepo;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean enterChatroom(long roomId, long uid, UserTypeEnum userType, Integer score) {
        chatroomRedisRepo.joinActiveRoomSet(roomId);
//        chatroomRedisRepo.enterChatroom(roomId, uid);

        if (userType == UserTypeEnum.VIP) {
//            chatroomRedisRepo.updateUserScore(roomId, uid, score);
            chatroomRedisRepo.saveUserVipInfo(roomId, uid);
        }
        return true;
    }

    @Override
    public boolean leaveChatroom(long roomId, long uid) {
        chatroomRedisRepo.joinActiveRoomSet(roomId);
//        chatroomRedisRepo.leaveChatroom(roomId, uid);
        return true;
    }

    @Override
    public void removeChatroomUsers(long roomId) {
        redisTemplate.delete(Lists.newArrayList(
                chatroomRedisRepo.getSonaChatroomAllUserKey(roomId),
                chatroomRedisRepo.getSonaSmallRangeKey(roomId)));
    }

    @Override
    public boolean kickoutChatroom(long roomId, long uid) {
        boolean ret = leaveChatroom(roomId, uid);
        if (ret) {
            chatroomRedisRepo.setKickOutUserFlag(roomId, uid);
        }
        return ret;
    }

    @Override
    public boolean updateUserScore(long roomId, long uid, int score) {
        chatroomRedisRepo.updateUserScore(roomId, uid, score);
        return true;
    }

    @Override
    public long getChatroomUserCount(long roomId) {
        return chatroomRedisRepo.getOnlineUserCount(roomId);
    }

    @Override
    public Map<Long, Long> batchGetChatroomUserCount(List<Long> roomIds) {
        if (CollectionUtils.isEmpty(roomIds)) {
            return Collections.emptyMap();
        }
        Map<Long, Long> onlineNums = multiGetOnlineNums(roomIds);
        Map<Long, Long> result = Maps.newHashMap();
        roomIds.forEach(roomId -> {
            long count = onlineNums.getOrDefault(roomId, 0L);
            result.put(roomId, count);
        });

        return result;
    }

    private Map<Long, Long> multiGetOnlineNums(List<Long> roomIds) {
        Map<Long, Long> map = Maps.newHashMap();
        int limit = 30;
        int size = roomIds.size();
        int index = 0;

        do {
            int endIndex = size - index >= limit ? limit + index : size;
            int len = Math.min(size - index, limit);
            List<Long> subRoomIds = roomIds.subList(index, endIndex);
            index = index + len;

            List<Object> executeResultList = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                connection.openPipeline();
                RedisSerializer<String> keySerializer = new StringRedisSerializer();
                subRoomIds.forEach(roomId -> {
                    String key = chatroomRedisRepo.getSonaChatroomAllUserKey(roomId);
                    byte[] redisKey = keySerializer.serialize(key);
                    connection.sCard(redisKey);
                });

                return null;
            });

            if (CollectionUtils.isEmpty(executeResultList)) {
                return map;
            }

            subRoomIds.forEach(roomId -> {
                int idx = subRoomIds.indexOf(roomId);
                long count = executeResultList.get(idx) == null ? 0 : Long.parseLong(executeResultList.get(idx) + "");
                map.put(roomId, count);
            });
        } while (index < size);

        return map;
    }

    @Override
    public PageResult<Long> getChatroomUserList(long roomId, String anchor, int limit) {
        int start = Integer.parseInt(anchor);
        int end = start + limit;
        long count = chatroomRedisRepo.getOnlineUserCount(roomId);
        if (start > count) {
            return PageResult.newPageResult(Lists.newArrayList(), true, count, anchor);
        }

        String smallRangeKey = chatroomRedisRepo.getSonaSmallRangeKey(roomId);
        Long smallRangeSize = redisTemplate.opsForZSet().zCard(smallRangeKey);
        if (smallRangeSize == null || start >= smallRangeSize) {
            List<String> uids = getUids(roomId, limit);
            return PageResult.newPageResult(uids.stream().map(Long::parseLong).collect(Collectors.toList()),
                    uids.size() < limit, count, anchor);
        }
        if (end <= smallRangeSize) {
            Set<String> uidSet = redisTemplate.opsForZSet().reverseRange(smallRangeKey, start, end - 1);
            return PageResult.newPageResult(uidSet.stream().map(Long::parseLong).collect(Collectors.toList()),
                    uidSet.size() < limit, count, String.valueOf(end));
        }

        Set<String> uidSet = redisTemplate.opsForZSet().reverseRange(smallRangeKey, start, smallRangeSize - 1);
        if (CollectionUtils.isEmpty(uidSet)) {
            uidSet = Sets.newHashSet();
        }
        List<String> uids = getUids(roomId, limit);
        uidSet.addAll(uids);
        return PageResult.newPageResult(uidSet.stream().map(Long::parseLong).collect(Collectors.toList()),
                uidSet.size() < limit || limit >= count, count, String.valueOf(end));
    }

    private List<String> getUids(long roomId, int limit) {
        return Lists.newArrayList(chatroomRedisRepo.getAllUserUidListByLimit(roomId, limit));
    }

    @Override
    public Map<Long, Boolean> isUserInChatroom(long roomId, List<Long> uids) {
        List<String> uidStringList = uids.stream().map(String::valueOf).collect(Collectors.toList());
        List<Boolean> list = sIsMembers(chatroomRedisRepo.getSonaChatroomAllUserKey(roomId), uidStringList);
        Map<Long, Boolean> result = Maps.newHashMap();
        for (int i = 0; i < uids.size(); i++) {
            result.put(uids.get(i), list.get(i));
        }
        return result;
    }

    private List<Boolean> sIsMembers(String key, List<String> members) {
        List<Object> result = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                RedisSerializer<String> keySerializer = new StringRedisSerializer();
                RedisSerializer<String> valueSerializer = new StringRedisSerializer();
                byte[] keyByte = keySerializer.serialize(key);

                members.forEach(member -> {
                    byte[] value = valueSerializer.serialize(member);
                    connection.sIsMember(keyByte, value);

                });

                return null;
            }

        });

        return result.stream().map(Boolean.TRUE::equals).collect(Collectors.toList());
    }
}
