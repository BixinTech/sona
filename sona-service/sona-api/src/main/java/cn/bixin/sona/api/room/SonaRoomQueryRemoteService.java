package cn.bixin.sona.api.room;

import cn.bixin.sona.common.annotation.CommonExecutor;
import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.dto.RoomConfigDTO;
import cn.bixin.sona.dto.RoomDTO;
import cn.bixin.sona.dto.RoomUserDTO;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public interface SonaRoomQueryRemoteService {

    /**
     * 查询房间信息
     * @param roomId
     * @return
     */
    @CommonExecutor(desc = "查询房间信息", printParam = true, printResponse = true)
    Response<RoomDTO> getRoom(@NotNull Long roomId);

    /**
     * 批量查询房间信息
     * @param roomIds
     * @return
     */
    @CommonExecutor(desc = "批量查询房间信息", printParam = true, printResponse = true)
    Response<Map<Long, RoomDTO>> getRoomBatch(@NotNull List<Long> roomIds);

    /**
     * 查询房间在线人数
     * @param roomId: 房间id
     * @return member count
     */
    @CommonExecutor(desc = "查询房间在线人数")
    Response<Long> getRoomMemberCount(long roomId);

    /**
     * 查询房间在线人数
     * @param roomIds: 房间id列表
     * @return
     */
    @CommonExecutor(desc = "批量查询房间在线人数")
    Response<Map<Long, Long>> batchGetRoomMemberCount(List<Long> roomIds);

    /**
     * 查询房间在线人员
     *
     * @param roomId: 房间id
     * @param anchor: 游标 默认0
     * @param limit:  查询数量
     * @return member list
     */
    @CommonExecutor(desc = "查询房间在线人数")
    Response<PageResult<RoomUserDTO>> getRoomMemberList(long roomId, String anchor, int limit);

    /**
     * 检查用户是否在线
     * @param roomId:房间id
     * @param uids:uids
     * @return
     */
    @CommonExecutor(desc = "检查用户是否在线", printParam = true, printResponse = true)
    Response<Map<Long, Boolean>> isUserInRoom(long roomId, List<Long> uids);

    @CommonExecutor(desc = "查询房间配置", printParam = true, printResponse = true)
    Response<RoomConfigDTO> getRoomConfig(long roomId);

    @CommonExecutor(desc = "查询在线房间", printParam = true, printResponse = true)
    Response<PageResult<RoomDTO>> getOnlineRoomList(String anchor, int limit);
}
