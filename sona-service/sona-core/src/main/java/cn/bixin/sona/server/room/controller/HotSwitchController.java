package cn.bixin.sona.server.room.controller;

import cn.bixin.sona.api.room.HotSwitchRemoteService;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.enums.RoomMixedEnum;
import cn.bixin.sona.server.room.manager.AudioSwitchManager;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class HotSwitchController implements HotSwitchRemoteService {
    @Resource
    private AudioSwitchManager audioSwitchManager;

    @Override
    public Response<Boolean> switchAudioSupplierRoom(long roomId, int mixed) {
        return Response.success(audioSwitchManager.roomSwitch(Lists.newArrayList(roomId), RoomMixedEnum.getRoomMixedEnum(mixed)));
    }
}
