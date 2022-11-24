package cn.bixin.sona.api.room;

import cn.bixin.sona.common.annotation.CommonExecutor;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.dto.RoomDetailInfoDTO;
import cn.bixin.sona.request.*;

import javax.validation.constraints.NotNull;

public interface SonaRoomRemoteService {

    /**
     * 创建房间
     *
     * @param request: {@link CreateRoomRequest}
     * @return info: {@link RoomDetailInfoDTO}
     *
     */
    @CommonExecutor(desc = "创建房间", printParam = true, printResponse = true)
    Response<RoomDetailInfoDTO> createRoom(@NotNull CreateRoomRequest request);

    /**
     * 打开房间
     *
     * @param request: {@link OpenCloseRoomRequest}
     * @return info: {@link RoomDetailInfoDTO}
     *
     */
    @CommonExecutor(desc = "打开房间", printParam = true, printResponse = true)
    Response<RoomDetailInfoDTO> openRoom(@NotNull OpenCloseRoomRequest request);

    /**
     * 关闭房间
     */
    @CommonExecutor(desc = "关闭房间", printParam = true, printResponse = true)
    Response<Boolean> closeRoom(@NotNull OpenCloseRoomRequest request);

    @CommonExecutor(desc = "进入房间", printParam = true, printResponse = true)
    Response<RoomDetailInfoDTO> enterRoom(@NotNull EnterRoomRequest request);

    @CommonExecutor(desc = "离开房间", printParam = true, printResponse = true)
    Response<Boolean> leaveRoom(@NotNull LeaveRoomRequest request);

    /**
     * 更新密码
     *
     * @param request
     * @return true update success, otherwise false
     *
     */
    @CommonExecutor(desc = "更新密码", printParam = true, printResponse = true)
    Response<Boolean> updatePassword(@NotNull UpdatePasswordRequest request);

    /**
     * 禁言用户
     *
     * @param request: {@link OperateRequest}
     * @return true mute success, otherwise false
     */
    @CommonExecutor(desc = "禁言用户", printParam = true, printResponse = true)
    Response<Boolean> muteUser(@NotNull OperateRequest request);

    /**
     * 取消禁言用户
     *
     * @param request: {@link OperateRequest}
     * @return true cancel mute success, otherwise false
     */
    @CommonExecutor(desc = "取消禁言用户", printParam = true, printResponse = true)
    Response<Boolean> cancelMuteUser(@NotNull OperateRequest request);

    /**
     * 拉黑用户
     *
     * @param request: {@link OperateRequest}
     * @return true block success, otherwise false
     */
    @CommonExecutor(desc = "拉黑用户", printParam = true, printResponse = true)
    Response<Boolean> blockUser(@NotNull OperateRequest request);

    /**
     * 取消拉黑用户
     *
     * @param request: {@link OperateRequest}
     * @return true block success, otherwise false
     */
    @CommonExecutor(desc = "取消拉黑用户", printParam = true, printResponse = true)
    Response<Boolean> cancelBlockUser(@NotNull OperateRequest request);

    /**
     * 踢出用户
     *
     * @param request: {@link OperateRequest}
     * @return true kick success, otherwise false
     */
    @CommonExecutor(desc = "踢出用户", printParam = true, printResponse = true)
    Response<Boolean> kickUser(@NotNull OperateRequest request);

    /**
     * 设置管理员
     *
     * @param request: {@link OperateRequest}
     * @return true set admin success, otherwise false
     */
    @CommonExecutor(desc = "设置管理员", printParam = true, printResponse = true)
    Response<Boolean> setAdmin(@NotNull OperateRequest request);

    /**
     * 取消管理员
     *
     * @param request: {@link OperateRequest}
     * @return true remove admin success, otherwise false
     */
    @CommonExecutor(desc = "取消管理员", printParam = true, printResponse = true)
    Response<Boolean> removeAdmin(@NotNull OperateRequest request);

    /**
     * 更新聊天室用户分值
     *
     * @param roomId 房间ID
     * @param uid 用户ID
     * @param score 新分值
     * @return
     */
    @CommonExecutor(desc = "更新聊天室用户分值", printParam = true, printResponse = true)
    Response<Boolean> updateChatroomUserScore(Long roomId, Long uid, int score);

    /**
     * 日志上报
     * @param request
     * @return
     */
    @CommonExecutor(desc = "日志上报")
    Response<Boolean> logReport(@NotNull LogReportRequest request);
}
