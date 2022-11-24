package cn.bixin.sona.server.room.service;

import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.dto.RoomDTO;
import cn.bixin.sona.request.CreateRoomRequest;
import cn.bixin.sona.request.OpenCloseRoomRequest;
import cn.bixin.sona.server.room.domain.db.ProductConfig;
import cn.bixin.sona.server.room.domain.db.Room;

import java.util.List;
import java.util.Map;

public interface RoomService {

    /**
     * 获取房间
     *
     * @param roomId
     * @return RoomDTO
     */
    RoomDTO getRoomByRoomId(long roomId);

    /**
     * 批量获取房间
     *
     * @param roomIds
     * @return RoomDTO
     */
    Map<Long, RoomDTO> batchGetRooms(List<Long> roomIds);

    /**
     * 创建聊天室
     * @param request: create chatroom request
     *
     * @return roomId: 内部房间Id
     */
    Room createRoom(CreateRoomRequest request, ProductConfig config);

    /**
     * 打开聊天室
     * @param request
     */
    void openRoom(OpenCloseRoomRequest request);

    /**
     * 关闭聊天室
     * @param request
     * @return
     */
    boolean closeRoom(OpenCloseRoomRequest request);

    /**
     * 修改密码
     *
     * @param roomId: 房间id(主键)
     * @param password
     *
     * @return true update success, otherwise false
     */
    boolean updatePassword(long roomId, String password);

    /**
     * 获取在线房间列表
     * @param anchor
     * @param limit
     * @return
     */
    PageResult<RoomDTO> getOnlineRoomList(String anchor, int limit);
}
