package cn.bixin.sona.session.api;


import cn.bixin.sona.common.annotation.CommonExecutor;
import cn.bixin.sona.common.dto.Response;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * @author qinwei
 */
public interface UserSessionRemoteService {

    @CommonExecutor(desc = "获取用户当前时刻的长连接channelId")
    Response<List<String>> getCurrentChannel(long uid);

    @CommonExecutor(desc = "获取用户当前时刻的长连接channelId")
    Response<Map<Long, List<String>>> getCurrentChannels(@Size(min = 1, max = 200) List<Long> uids);

    @CommonExecutor(desc = "查询用户当前是否在线")
    Response<Boolean> getOnlineState(long uid);

    @CommonExecutor(desc = "查询用户当前是否在线")
    Response<Map<Long, Boolean>> getOnlineStates(@Size(min = 1, max = 200) List<Long> uids);

    @CommonExecutor(desc = "查询用户当前在哪些聊天室房间")
    Response<List<String>> getChatRoomOnlineState(long uid);

    @CommonExecutor(desc = "查询用户当前在哪些聊天室房间")
    Response<Map<Long, List<String>>> getChatRoomOnlineStates(@Size(min = 1, max = 200) List<Long> uids);

}
