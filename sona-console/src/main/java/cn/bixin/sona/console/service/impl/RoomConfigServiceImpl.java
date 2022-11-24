package cn.bixin.sona.console.service.impl;

import cn.bixin.sona.console.service.RoomConfigService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import cn.bixin.sona.console.mapper.RoomConfigMapper;
import cn.bixin.sona.console.domain.db.RoomConfig;

import java.util.Collection;
import java.util.List;

@Service
public class RoomConfigServiceImpl implements RoomConfigService {

    @Resource
    private RoomConfigMapper roomConfigMapper;

    @Override
    public List<RoomConfig> selectByStreamSupplierAndPullMode(String streamSupplier, String pullMode) {
        return roomConfigMapper.selectByStreamSupplierAndPullMode(streamSupplier, pullMode);
    }

    @Override
    public List<RoomConfig> selectByRoomIds(Collection<Long> roomIdCollection) {
        return roomConfigMapper.selectByRoomIdIn(roomIdCollection);
    }
}
