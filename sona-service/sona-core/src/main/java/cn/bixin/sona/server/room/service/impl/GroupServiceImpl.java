package cn.bixin.sona.server.room.service.impl;

import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.server.room.domain.db.GroupMember;
import cn.bixin.sona.server.room.domain.db.GroupUserCount;
import cn.bixin.sona.server.room.mapper.RoomGroupMemberMapper;
import cn.bixin.sona.server.room.service.GroupService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    @Resource
    private RoomGroupMemberMapper roomGroupMemberMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_GROUP_MEMBER_COUNT = "gmc:s";
    private String getKeyGroupMemberCount(long roomId) {
        return Joiner.on(":").join(KEY_GROUP_MEMBER_COUNT, roomId);
    }

    @Override
    public boolean enterGroup(long roomId, String productCode, long uid) {
        GroupMember groupMember = roomGroupMemberMapper.selectByRoomIdAndUid(roomId, uid);
        if (groupMember != null) {
            return true;
        }

        return roomGroupMemberMapper.insertSelective(GroupMember.wrapCreateObj(roomId, uid, productCode)) > 0;
    }

    @Override
    public boolean leaveGroup(long roomId, long uid) {
        return roomGroupMemberMapper.updateStatusLeave(roomId, uid) > 0;
    }

    @Override
    public boolean removeGroupMembers(long roomId) {
        roomGroupMemberMapper.disbandMember(roomId);
        return true;
    }

    @Override
    public long getGroupMemberCount(long roomId) {
        //todo 人数缓存设计待优化
        String cacheResult = redisTemplate.opsForValue().get(getKeyGroupMemberCount(roomId));
        if (StringUtils.isBlank(cacheResult)) {
            int count = roomGroupMemberMapper.selectGroupMemberCountByRoomId(roomId);
            redisTemplate.opsForValue().set(getKeyGroupMemberCount(roomId), String.valueOf(count), 1, TimeUnit.SECONDS);
            return count;
        }
        return Long.parseLong(cacheResult);
    }

    @Override
    public Map<Long, Long> batchGetGroupMemberCount(List<Long> roomIds) {
        if (CollectionUtils.isEmpty(roomIds)) {
            return Collections.emptyMap();
        }
        List<String> countKeys = roomIds.stream().map(this::getKeyGroupMemberCount).collect(Collectors.toList());
        List<String> counts = redisTemplate.opsForValue().multiGet(countKeys);
        Map<Long, Long> result = Maps.newHashMap();
        List<Long> roomIdsForDB = Lists.newArrayList();

        for (int i = 0; i < roomIds.size(); i++) {
            if (counts.get(i) != null) {
                result.put(roomIds.get(i), Long.valueOf(counts.get(i)));
            } else {
                roomIdsForDB.add(roomIds.get(i));
            }
        }

        result.putAll(getGroupMemberCountFromDB(roomIdsForDB));
        return result;
    }

    private Map<Long, Long> getGroupMemberCountFromDB(List<Long> roomIdsForDB) {
        if (CollectionUtils.isEmpty(roomIdsForDB)) {
            return Collections.emptyMap();
        }
        Map<Long, Long> result = Maps.newHashMap();
//        List<GroupMember> groupMembers = roomGroupMemberMapper.selectGroupMemberByRoomIds(roomIdsForDB);
//
//        groupMembers.forEach(m -> {
//            Long count = result.get(m.getRoomId());
//            if (count == null) {
//                result.put(m.getRoomId(), 1L);
//            } else {
//                result.put(m.getRoomId(), count + 1);
//            }
//        });
        List<GroupUserCount> groupUserCounts = roomGroupMemberMapper.selectGroupUserCountByRoomIds(roomIdsForDB);
        if (CollectionUtils.isNotEmpty(groupUserCounts)) {
            groupUserCounts.forEach(guc -> result.put(guc.getRoomId(), guc.getCount()));
        }

        roomIdsForDB.forEach(roomId -> result.putIfAbsent(roomId, 0L));
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            RedisSerializer<String> keySerializer = new StringRedisSerializer();
            RedisSerializer<String> valueSerializer = new StringRedisSerializer();
            result.forEach((roomId, count) -> {
                String key = getKeyGroupMemberCount(roomId);
                byte[] redisKey = keySerializer.serialize(key);
                byte[] redisValue = valueSerializer.serialize(String.valueOf(count));
                connection.set(redisKey, redisValue);
                connection.expire(redisKey, 1);
            });
            return null;
        });

        return result;
    }

    @Override
    public PageResult<Long> getRoomMemberList(long roomId, String anchor, int limit) {
        long anchorNum = Long.parseLong(anchor);

        long count = getGroupMemberCount(roomId);
        if (count == 0) {
            return PageResult.newPageResult(Collections.emptyList(), true);
        }

        List<GroupMember> memberList = roomGroupMemberMapper.selectGroupMemberListByRoomId(roomId, anchorNum, limit);
        return PageResult.newPageResult(memberList.stream().map(GroupMember::getUid).collect(Collectors.toList()),
                memberList.size() < limit || limit >= count, count, String.valueOf(memberList.get(memberList.size() - 1).getId()));
    }

    @Override
    public Map<Long, Boolean> isUserInGroup(long roomId, List<Long> uids) {
        List<GroupMember> memberList = roomGroupMemberMapper.selectByRoomIdAndUids(roomId, uids);
        if (org.springframework.util.CollectionUtils.isEmpty(memberList)) {
            return Collections.emptyMap();
        }

        Map<Long, GroupMember> uid2GroupMemberMap = memberList.stream().collect(Collectors.toMap(GroupMember::getUid, Function.identity(), (v1, v2) -> v1));
        Map<Long, Boolean> result = Maps.newHashMap();
        uids.forEach(uid -> result.put(uid, uid2GroupMemberMap.get(uid) != null));

        return result;
    }
}
