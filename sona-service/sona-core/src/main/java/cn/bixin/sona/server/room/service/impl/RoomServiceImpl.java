package cn.bixin.sona.server.room.service.impl;

import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.common.exception.YppRunTimeException;
import cn.bixin.sona.common.util.IdGenerator;
import cn.bixin.sona.dto.RoomDTO;
import cn.bixin.sona.request.CreateRoomRequest;
import cn.bixin.sona.request.OpenCloseRoomRequest;
import cn.bixin.sona.server.exception.ExceptionCode;
import cn.bixin.sona.server.room.domain.convert.RoomDTOConverter;
import cn.bixin.sona.server.room.domain.db.ProductConfig;
import cn.bixin.sona.server.room.domain.db.Room;
import cn.bixin.sona.server.room.mapper.RoomMapper;
import cn.bixin.sona.server.room.service.RoomService;
import com.alibaba.fastjson.JSON;
import com.dianping.cat.Cat;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
public class RoomServiceImpl implements RoomService {

    private static final Logger log = LoggerFactory.getLogger(RoomServiceImpl.class);

    @Resource
    private IdGenerator idGenerator;
    @Resource
    private RoomMapper roomMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public RoomDTO getRoomByRoomId(long roomId) {
        log.debug("getRoomByRoomId roomId:{}", roomId);

        String value = redisTemplate.opsForValue().get(getKeyChatroomInfo(roomId));
        if (StringUtils.isNotBlank(value)) {
            return RoomDTOConverter.convertDO(JSON.parseObject(value, Room.class));
        }

        Room room = roomMapper.getRoomByRoomId(roomId);
        if (room == null) {
            return null;
        }
        redisTemplate.opsForValue().set(getKeyChatroomInfo(roomId), JSON.toJSONString(room), 1, TimeUnit.MINUTES);
        return RoomDTOConverter.convertDO(room);
    }

    @Override
    public Map<Long, RoomDTO> batchGetRooms(List<Long> roomIds) {
        List<String> roomKeys = roomIds.stream().map(this::getKeyChatroomInfo).collect(Collectors.toList());
        List<String> redisList = redisTemplate.opsForValue().multiGet(roomKeys);
        Map<Long, RoomDTO> ret = redisList.stream().filter(StringUtils::isNotBlank).map(value -> RoomDTOConverter.convertDO(JSON.parseObject(value, Room.class)))
                .collect(Collectors.toMap(RoomDTO::getRoomId, Function.identity(), (v1, v2) -> v1));

        List<Long> roomIdsForDB = roomIds.stream().filter(roomId -> ret.get(roomId) == null).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(roomIdsForDB)) {
            return ret;
        }
        List<Room> rooms = roomMapper.getRoomsByRoomIds(roomIdsForDB);
        if (CollectionUtils.isEmpty(roomIdsForDB)) {
            Cat.logEvent("MISS_ROOM_ID", JSON.toJSONString(roomIdsForDB));
            return ret;
        }
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            RedisSerializer<String> keySerializer = new StringRedisSerializer();
            RedisSerializer<String> valueSerializer = new StringRedisSerializer();
            rooms.forEach(room -> {
                byte[] redisKey = keySerializer.serialize(getKeyChatroomInfo(room.getRoomId()));
                byte[] redisValue = valueSerializer.serialize(JSON.toJSONString(room));
                connection.set(redisKey, redisValue);
                connection.expire(redisKey, 60); // 1 min
            });
            return null;
        });

        ret.putAll(rooms.stream().collect(Collectors.toMap(Room::getRoomId, RoomDTOConverter::convertDO, (v1, v2) -> v1)));
        return ret;
    }

    @Override
    public Room createRoom(CreateRoomRequest request, ProductConfig config) {
        try {
            long roomId = idGenerator.id();
            Room record = Room.wrapCreateObj(request, roomId, config.getImModule());
            roomMapper.insertSelective(record);

            log.debug("createRoom room:{}", JSON.toJSONString(record));
            return record;
        } catch (Exception e) {
            log.error("createRoom error.", e);
            throw new YppRunTimeException(ExceptionCode.CREATE_CHATROOM_ERROR);
        }
    }

    @Override
    public void openRoom(OpenCloseRoomRequest request) {
        try {
            log.debug("openRoom request:{}", JSON.toJSONString(request));
            roomMapper.openRoom(request.getRoomId());
            redisTemplate.delete(Lists.newArrayList(getKeyChatroomInfo(request.getRoomId())));
        } catch (Exception e) {
            log.error("openRoom error", e);
            throw new YppRunTimeException(ExceptionCode.OPEN_CLOSE_CHATROOM_ERROR);
        }
    }

    @Override
    public boolean closeRoom(OpenCloseRoomRequest request) {
        try {
            log.debug("openRoom request:{}", JSON.toJSONString(request));
            int ret = roomMapper.closeRoom(request.getRoomId());
            redisTemplate.delete(Lists.newArrayList(getKeyChatroomInfo(request.getRoomId())));
            return ret > 0;
        } catch (Exception e) {
            log.error("openRoom error", e);
            throw new YppRunTimeException(ExceptionCode.OPEN_CLOSE_CHATROOM_ERROR);
        }
    }

    @Override
    public boolean updatePassword(long roomId, String password) {
        int ret = roomMapper.updatePassword(roomId, password);
        redisTemplate.delete(Lists.newArrayList(getKeyChatroomInfo(roomId)));
        return ret > 0;
    }

    @Override
    public PageResult<RoomDTO> getOnlineRoomList(String anchor, int limit) {

        List<Room> roomList = roomMapper.selectOnlineRoomList(anchor, limit);
        if (CollectionUtils.isEmpty(roomList)) {
            return PageResult.newPageResult(Collections.emptyList(), true);
        }

        PageResult<RoomDTO> pageResult = new PageResult();
        pageResult.setAnchor(roomList.get(roomList.size() - 1).getRoomId() + "");
        pageResult.setCount(Long.valueOf(roomMapper.countOnlineRoomList() + ""));

        List<RoomDTO> roomDTOList = Lists.newArrayList();
        roomList.forEach(each -> {

            RoomDTO dto = new RoomDTO();
            BeanUtils.copyProperties(each, dto);
            roomDTOList.add(dto);
        });
        pageResult.setList(roomDTOList);
        pageResult.setEnd(roomList.size() < limit);

        return pageResult;
    }

    private static final String KEY_CHATROOM_INFO = "ci:s";

    private String getKeyChatroomInfo(long roomId) {
        return Joiner.on(":").join(KEY_CHATROOM_INFO, roomId);
    }
}
