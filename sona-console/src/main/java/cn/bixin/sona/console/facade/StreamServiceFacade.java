package cn.bixin.sona.console.facade;

import cn.bixin.sona.api.room.HotSwitchRemoteService;
import cn.bixin.sona.common.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StreamServiceFacade {
    @DubboReference
    private HotSwitchRemoteService hotSwitchRemoteService;

    public Boolean switchAudioSupplierRoom(long roomId, int mixed){
        Response<Boolean> response = hotSwitchRemoteService.switchAudioSupplierRoom(roomId, mixed);
        if (!response.isSuccess() || response.getResult() == null){
            return false;
        }
        return response.getResult();
    }
}
