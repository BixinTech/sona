package cn.bixin.sona.api.socket;


import cn.bixin.sona.api.socket.request.BatchChatroomMsgRequest;
import cn.bixin.sona.api.socket.request.ChatroomMsgRequest;
import cn.bixin.sona.api.socket.request.GroupMsgRequest;
import cn.bixin.sona.common.annotation.CommonExecutor;
import cn.bixin.sona.common.dto.Response;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 房间相关操作
 *
 * @author qinwei
 */
public interface RoomMessageRemoteService {

    @CommonExecutor(desc = "关闭聊天室", printParam = true, printResponse = true)
    Response<Boolean> closeChatroom(@NotEmpty String roomId);

    @CommonExecutor(desc = "批量踢出聊天室", printParam = true, printResponse = true)
    Response<Boolean> batchKickOutChatroom(@NotNull ChatroomMsgRequest request, @Size(min = 1, max = 200) List<Long> toUids);

    @CommonExecutor(desc = "发送普通聊天室消息", printParam = true, printResponse = true)
    Response<Boolean> sendChatroom(@NotNull ChatroomMsgRequest request);

    @CommonExecutor(desc = "批量发送普通聊天室消息", printParam = true, printResponse = true)
    Response<Boolean> batchSendChatroom(@NotNull BatchChatroomMsgRequest request);

    @CommonExecutor(desc = "给所有聊天室发送消息", printParam = true, printResponse = true)
    Response<Boolean> globalSendChatroom(@NotNull BatchChatroomMsgRequest request);

    @CommonExecutor(desc = "发送聊天室消息给指定批量用户", printParam = true, printResponse = true)
    Response<Boolean> batchSendChatroomMember(@NotNull ChatroomMsgRequest request, @Size(min = 1, max = 200) List<Long> toUids);

    @CommonExecutor(desc = "发送群组消息", printParam = true, printResponse = true)
    Response<Boolean> sendGroup(@NotNull GroupMsgRequest request);

    @CommonExecutor(desc = "发送群组消息", printParam = true, printResponse = true)
    Response<Boolean> sendMsgToGroupMembers(@NotNull GroupMsgRequest request, @Size(min = 1, max = 200) List<Long> toUids);

}
