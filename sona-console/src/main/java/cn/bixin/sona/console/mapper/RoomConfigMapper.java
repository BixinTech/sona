package cn.bixin.sona.console.mapper;
import java.util.Collection;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import cn.bixin.sona.console.domain.db.RoomConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoomConfigMapper {

    List<RoomConfig> selectByStreamSupplierAndPullMode(@Param("streamSupplier")String streamSupplier,@Param("pullMode")String pullMode);

    List<RoomConfig> selectByRoomIdIn(@Param("roomIdCollection")Collection<Long> roomIdCollection);


}