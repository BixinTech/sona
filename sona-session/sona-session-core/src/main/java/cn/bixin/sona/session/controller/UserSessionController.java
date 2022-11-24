package cn.bixin.sona.session.controller;

import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.session.api.UserSessionRemoteService;
import cn.bixin.sona.session.service.ServerStatCache;
import cn.bixin.sona.session.utils.CacheKey;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qinwei
 */
@DubboService
public class UserSessionController implements UserSessionRemoteService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ServerStatCache serverStatCache;

    @Override
    public Response<List<String>> getCurrentChannel(long uid) {
        Set<String> members = stringRedisTemplate.opsForSet().members(CacheKey.getUidKey(String.valueOf(uid)));
        return Response.success(new ArrayList<>(members));
    }

    @Override
    public Response<Map<Long, List<String>>> getCurrentChannels(List<Long> uids) {
        RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
        List<byte[]> keys = Lists.newArrayListWithCapacity(uids.size());
        for (Long uid : uids) {
            keys.add(serializer.serialize(CacheKey.getUidKey(String.valueOf(uid))));
        }
        RedisCallback<List<Object>> callback = connection -> {
            for (byte[] key : keys) {
                connection.sMembers(key);
            }
            return null;
        };
        List<Object> pipelineResult = stringRedisTemplate.executePipelined(callback);
        Map<Long, List<String>> result = new HashMap<>(uids.size());
        for (int i = 0; i < uids.size(); i++) {
            Set<String> channels = (Set<String>) pipelineResult.get(i);
            result.put(uids.get(i), new ArrayList<>(channels));
        }
        return Response.success(result);
    }

    @Override
    public Response<Boolean> getOnlineState(long uid) {
        Long size = stringRedisTemplate.opsForSet().size(CacheKey.getUidKey(String.valueOf(uid)));
        return Response.success(size > 0);
    }

    @Override
    public Response<Map<Long, Boolean>> getOnlineStates(List<Long> uids) {
        RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
        List<byte[]> keys = Lists.newArrayListWithCapacity(uids.size());
        for (Long uid : uids) {
            keys.add(serializer.serialize(CacheKey.getUidKey(String.valueOf(uid))));
        }
        RedisCallback<List<Object>> callback = connection -> {
            for (byte[] key : keys) {
                connection.sCard(key);
            }
            return null;
        };
        List<Object> pipelineResult = stringRedisTemplate.executePipelined(callback);
        Map<Long, Boolean> result = new HashMap<>(uids.size());
        for (int i = 0; i < uids.size(); i++) {
            Long size = (Long) pipelineResult.get(i);
            result.put(uids.get(i), size > 0);
        }
        return Response.success(result);
    }

    @Override
    public Response<List<String>> getChatRoomOnlineState(long uid) {
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        List<String> result = opsForHash.entries(CacheKey.getSonaUidKey(String.valueOf(uid)))
                .entrySet()
                .stream()
                .filter(entry -> serverStatCache.judgeChannelValid(entry.getKey()))
                .map(Map.Entry::getValue)
                .distinct()
                .collect(Collectors.toList());
        return Response.success(result);
    }

    @Override
    public Response<Map<Long, List<String>>> getChatRoomOnlineStates(List<Long> uids) {
        RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
        List<byte[]> keys = Lists.newArrayListWithCapacity(uids.size());
        for (Long uid : uids) {
            keys.add(serializer.serialize(CacheKey.getSonaUidKey(String.valueOf(uid))));
        }
        RedisCallback<List<Object>> callback = connection -> {
            for (byte[] key : keys) {
                connection.hGetAll(key);
            }
            return null;
        };
        List<Object> pipelineResult = stringRedisTemplate.executePipelined(callback);
        Map<Long, List<String>> result = new HashMap<>(uids.size());
        for (int i = 0; i < uids.size(); i++) {
            Map<String, String> channelRooms = (Map<String, String>) pipelineResult.get(i);
            List<String> list = channelRooms.entrySet()
                    .stream()
                    .filter(entry -> serverStatCache.judgeChannelValid(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .distinct()
                    .collect(Collectors.toList());
            result.put(uids.get(i), list);
        }
        return Response.success(result);
    }

}
