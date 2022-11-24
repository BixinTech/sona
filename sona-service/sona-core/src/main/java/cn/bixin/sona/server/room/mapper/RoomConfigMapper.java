package cn.bixin.sona.server.room.mapper;

import cn.bixin.sona.server.room.domain.db.RoomConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoomConfigMapper {

    RoomConfig findRoomConfig(@Param("roomId") long roomId);

    List<RoomConfig> findRoomConfigBatch(@Param("roomIds") List<Long> roomIds);

    int insertSelective(RoomConfig roomConfig);

    int updateStreamConfigAudios(List<RoomConfig> config);
}
