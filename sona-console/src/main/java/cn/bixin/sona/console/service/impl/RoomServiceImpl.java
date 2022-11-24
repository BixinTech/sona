package cn.bixin.sona.console.service.impl;

import cn.bixin.sona.console.service.RoomService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import cn.bixin.sona.console.domain.db.Room;
import cn.bixin.sona.console.mapper.RoomMapper;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Resource
    private RoomMapper roomMapper;

    @Override
    public int deleteByPrimaryKey(Long roomId) {
        return roomMapper.deleteByPrimaryKey(roomId);
    }

    @Override
    public int insert(Room record) {
        return roomMapper.insert(record);
    }

    @Override
    public int insertSelective(Room record) {
        return roomMapper.insertSelective(record);
    }

    @Override
    public Room selectByPrimaryKey(Long roomId) {
        return roomMapper.selectByPrimaryKey(roomId);
    }

    @Override
    public int updateByPrimaryKeySelective(Room record) {
        return roomMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Room record) {
        return roomMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Room> selectByRoomIds(List<Long> roomIds) {
        return roomMapper.selectByRoomIdIn(roomIds);
    }

}
