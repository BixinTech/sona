package cn.bixin.sona.api.room;

import cn.bixin.sona.common.annotation.CommonExecutor;
import cn.bixin.sona.common.dto.Response;

public interface HotSwitchRemoteService {
    /**
     * 切换音频运营商单房间
     *
     * @param roomId 房间id
     * @param mixed  模式
     * @return
     */
    @CommonExecutor(desc = "切换音频运营商单房间", printParam = true, printResponse = true)
    Response<Boolean> switchAudioSupplierRoom(long roomId, int mixed);

}
