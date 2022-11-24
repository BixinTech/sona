package cn.bixin.sona.server.room.mapper;

import cn.bixin.sona.server.room.domain.db.RoomManagementLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoomManagementLogMapper {
    int insertSelective(RoomManagementLog record);
}
