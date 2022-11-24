package cn.bixin.sona.console.service;

import cn.bixin.sona.console.domain.db.RoomConfig;

import java.util.Collection;
import java.util.List;

public interface RoomConfigService{

    List<RoomConfig> selectByStreamSupplierAndPullMode(String streamSupplier, String pullMode);

    List<RoomConfig> selectByRoomIds(Collection<Long> roomIdCollection);
}
