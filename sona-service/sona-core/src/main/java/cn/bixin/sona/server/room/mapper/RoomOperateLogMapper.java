package cn.bixin.sona.server.room.mapper;

import cn.bixin.sona.server.room.domain.db.RoomOperateLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoomOperateLogMapper {
    int insertSelective(RoomOperateLog record);
}