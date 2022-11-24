package cn.bixin.sona.api.im;

import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.common.annotation.CommonExecutor;
import cn.bixin.sona.common.dto.Response;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 专门发送 ack 的点对点消息
 *
 * @author qinwei
 */
public interface RouteAckMessageService {

    @CommonExecutor(desc = "发送ack点对点消息")
    Response<Boolean> sendAckMessage(@NotNull RoomMessageRequest request, @Size(min = 1, max = 100) List<Long> uids);

}
