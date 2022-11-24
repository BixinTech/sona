package cn.bixin.sona.console.service;

import cn.bixin.sona.console.domain.db.Room;

import java.util.List;

public interface RoomService{


    int deleteByPrimaryKey(Long roomId);

    int insert(Room record);

    int insertSelective(Room record);

    Room selectByPrimaryKey(Long roomId);

    int updateByPrimaryKeySelective(Room record);

    int updateByPrimaryKey(Room record);

    List<Room> selectByRoomIds(List<Long> roomIds);
}
