package cn.bixin.sona.server.room.domain.enums;

public enum TransactionTypeEnum {
    /**
     * 创建房间
     */
    TR_ROOM_CREATE("TR_ROOM_CREATE", "创建房间"),
    /**
     * 打开房间
     */
    TR_ROOM_OPEN("TR_ROOM_OPEN", "打开房间"),
    /**
     * 进入房间
     */
    TR_ROOM_ENTER("TR_ROOM_ENTER", "进入房间"),
    /**
     * 关闭房间
     */
    TR_ROOM_CLOSE("TR_ROOM_CLOSE", "关闭房间"),
    /**
     * 离开房间
     */
    TR_ROOM_LEAVE("TR_ROOM_LEAVE", "离开房间"),
    /**
     * 创建群组
     */
    TR_GROUP_CREATE("TR_GROUP_CREATE", "创建群组"),
    /**
     * 打开群组
     */
    TR_GROUP_OPEN("TR_GROUP_OPEN", "打开群组"),
    /**
     * 进入群组
     */
    TR_GROUP_ENTER("TR_GROUP_ENTER", "进入群组"),
    /**
     * 关闭群组
     */
    TR_GROUP_CLOSE("TR_GROUP_CLOSE", "关闭群组"),
    /**
     * 离开群组
     */
    TR_GROUP_LEAVE("TR_GROUP_LEAVE", "离开群组"),
    /**
     * 修改聊天室状态
     */
    TR_ROOM_STATUS("TR_ROOM_STATUS", "修改聊天室状态"),
    /**
     * 修改群组状态
     */
    TR_GROUP_STATUS("TR_GROUP_STATUS", "修改群组状态"),
    /**
     * 获取房间列表
     */
    TR_ROOM_ONLINE_USER("TR_ROOM_ONLINE_USER", "获取房间列表"),
    /**
     * 获取聊天室信息
     */
    TR_ROOM_DETAIL("TR_ROOM_DETAIL", "获取聊天室信息"),
    /**
     * 聊天室踢人
     */
    TR_ROOM_KICK_OUT("TR_ROOM_KICK_OUT", "聊天室踢人"),
    /**
     * 设置用户角色
     */
    TR_SET_USER_ROLE("TR_SET_USER_ROLE", "设置用户角色"),
    /**
     * 增加群组成员
     */
    TR_GROUP_ADD("TR_GROUP_ADD", "增加群组成员"),
    /**
     * 群组踢人
     */
    TR_GROUP_KICK_OUT("TR_GROUP_KICK_OUT", "群组踢人"),
    /**
     * 移除群组
     */
    TR_GROUP_REMOVE("TR_GROUP_REMOVE", "移除群组"),
    /**
     * 设置群组管理员
     */
    TR_GROUP_SET_ADMIN("TR_GROUP_SET_ADMIN", "设置群组管理员"),
    /**
     * 移除群组管理员
     */
    TR_GROUP_REMOVE_ADMIN("TR_GROUP_REMOVE_ADMIN", "移除群组管理员"),
    /**
     * 房间明细信息
     */
    YX_ROOM_DETAIL("YX_ROOM_DETAIL", "房间明细信息"),
    /**
     * 长连进入房间
     */
    MERCURY_ENTER_ROOM_NEW("MERCURY_ENTER_ROOM_NEW", "新版长连进入房间"),
    /**
     * 长连离开房间
     */
    MERCURY_LEAVE_ROOM_NEW("MERCURY_LEAVE_ROOM_NEW", "新版长连离开房间"),
    /**
     * 异步创建聊天室失败
     */
    ASYNC_CREATE_CHATROOM_ERROR("ASYNC_CREATE_CHATROOM_ERROR", "异步创建聊天室失败"),
    /**
     * 异步创建群组失败
     */
    ASYNC_CREATE_GROUP_ERROR("ASYNC_CREATE_GROUP_ERROR", "异步创建群组失败"),

    /**
     * 异步打开聊天室失败
     */
    ASYNC_OPEN_CHATROOM_ERROR("ASYNC_OPEN_CHATROOM_ERROR", "异步打开聊天室失败"),
    /**
     * 异步关闭聊天室失败
     */
    ASYNC_CLOSE_CHATROOM_ERROR("ASYNC_CLOSE_CHATROOM_ERROR", "异步关闭聊天室失败"),
    /**
     * 异步关闭群组失败
     */
    ASYNC_CLOSE_GROUP_ERROR("ASYNC_CLOSE_GROUP_ERROR", "异步关闭群组失败"),
    /**
     * 异步进入群组失败
     */
    ASYNC_ENTER_GROUP_ERROR("ASYNC_ENTER_GROUP_ERROR", "异步进入群组失败"),
    /**
     * 异步离开群组失败
     */
    ASYNC_LEAVE_GROUP_ERROR("ASYNC_LEAVE_GROUP_ERROR", "异步离开群组失败")
    ;

    private final String code;
    private final String desc;

    TransactionTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
