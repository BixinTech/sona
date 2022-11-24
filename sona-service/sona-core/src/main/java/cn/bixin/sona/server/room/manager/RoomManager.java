package cn.bixin.sona.server.room.manager;

import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.dto.RoomConfigDTO;
import cn.bixin.sona.dto.RoomDTO;
import cn.bixin.sona.dto.RoomDetailInfoDTO;
import cn.bixin.sona.dto.RoomUserDTO;
import cn.bixin.sona.request.*;

import java.util.List;
import java.util.Map;

public interface RoomManager {

    /**
     * 查询房间
     */
    RoomDTO getRoom(long roomId);

    /**
     * 批量查询房间
     */
    Map<Long, RoomDTO> getRoomBatch(List<Long> roomIds);

    /**
     * 查询房间人数
     */
    long getRoomMemberCount(long roomId);

    /**
     * 批量查询房间人数
     * @param roomIds
     * @return
     */
    Map<Long, Long> batchGetRoomMemberCount(List<Long> roomIds);

    /**
     * 查询房间在线人员
     *
     * @param roomId: 房间id
     * @param anchor: 游标 默认0
     * @param limit: 查询数量
     *
     * @return member list
     */
    PageResult<RoomUserDTO> getRoomMemberList(long roomId, String anchor, int limit);

    /**
     * 查询用户是否在线
     * @param roomId: roomId
     * @param uids: uids
     * @return map
     */
    Map<Long, Boolean> isUserInRoom(long roomId, List<Long> uids);

    /**
     * 创建房间
     */
    RoomDetailInfoDTO createRoom(CreateRoomRequest request);

    /**
     * 打开房间
     */
    RoomDetailInfoDTO openRoom(OpenCloseRoomRequest request);

    /**
     * 关闭房间
     */
    boolean closeRoom(OpenCloseRoomRequest request);

    /**
     * 进入房间
     */
    RoomDetailInfoDTO enterRoom(EnterRoomRequest request);

    /**
     * 离开房间
     */
    boolean leaveRoom(LeaveRoomRequest request);

    /**
     * 更新密码
     */
    boolean updatePassword(UpdatePasswordRequest request);

    /**
     * 禁言或取消禁言用户
     */
    boolean muteOrCancelUser(OperateRequest request);

    /**
     * 拉黑或取消拉黑用户
     */
    boolean blockOrCancelUser(OperateRequest request);

    /**
     * 踢出用户
     */
    boolean kickUser(OperateRequest request);

    /**
     * 设置或取消管理员
     */
    boolean setOrRemoveAdmin(OperateRequest request);

    /**
     * 更新聊天室用户分值
     *
     * @param roomId 房间ID
     * @param uid 用户uid
     * @param score 用户分值
     * @return 是否成功
     */
    boolean updateChatroomUserScore(long roomId, long uid, int score);

    /**
     * 获取房间配置
     * @param roomId
     * @return
     */
    RoomConfigDTO getRoomConfig(long roomId);

    /**
     * 获取打开状态的房间
     */
    PageResult<RoomDTO> getOnlineRoomList(String anchor, int limit);
}
