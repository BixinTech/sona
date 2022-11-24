package cn.bixin.sona.console.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Collection;

import cn.bixin.sona.console.domain.db.Room;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoomMapper {
    int deleteByPrimaryKey(Long roomId);

    int insert(Room record);

    int insertSelective(Room record);

    Room selectByPrimaryKey(Long roomId);

    int updateByPrimaryKeySelective(Room record);

    int updateByPrimaryKey(Room record);

    List<Room> selectByRoomIdIn(@Param("roomIdCollection")Collection<Long> roomIdCollection);


}