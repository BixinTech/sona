package cn.bixin.sona.console.exception;

import cn.bixin.sona.common.dto.Code;
import cn.bixin.sona.common.dto.Response;

public enum ExceptionCode implements Code {
    /**
     * 返回错误码定义
     */

    ERROR_PARAM("8010","参数错误"),
    NULL_PARAM("8011","参数为空"),
    EMPTY_PARAM("8012","缺少参数"),
    SERVER_ERROR("9000", "服务器异常"),
    DATA_NOT_FOUND("8014","数据没有找到"),
    PARAM_OVER_MAX("8015","参数超过最大长度"),
    ERROR_AUTH("8016", "没有权限操作"),
    ERROR_ACCID("8017", "获取accId失败"),
    ERROR_PASSWORD("8018", "密码错误"),
    OWNER_NOT_FOUND("8019", "房主没有找到"),
    ERROR_SEND_MESSAGE("8020", "发送消息失败"),
    PARAMETER_LENGTH_IS_TOO_LONG("8021","参数长度过长"),
    HTTP_ERROR("8022", "http请求异常"),
    DIAMOND_NOT_ENOUGH("8050", "账户钻石不足"),
    APP_VERSION_IS_LOW("8023", "当前版本过低，请升级版本"),

    CHATROOM_INVALID("10010", "聊天室无效"),
    CREATE_CHATROOM_ERROR("10011", "创建房间失败"),
    CHECK_ONLINE_ROOM_MEMBER_ERROR("10012", "检查房间在线人员失败"),
    OPEN_CLOSE_CHATROOM_ERROR("10013", "开关房间失败"),
    CREATOR_NOT_SAME_ERROR("10014", "操作人不是房主"),
    CHATROOM_EXISTS("10015", "房间已经存在"),
    CHATROOM_NOT_EXISTS("10016", "房间不存在"),
    KICK_OUT_ROOM_MEMBER_ERROR("10017", "房间踢人失败"),
    GET_ONLINE_ROOM_MEMBER_ERROR("10018", "获取在线成员失败"),
    IM_CHATROOM_NOT_FOUNT("10019", "im房间查询未找到"),
    RTC_RECORD_NOT_EXISTS("10020", "连麦记录不存在"),
    RTC_RECORD_REPETITION("10021", "连麦重复上报"),
    RTC_ROOM_NOT_EXISTS("10022", "rtc房间不存在"),
    SET_USER_ROLE_ERROR("10023", "设置用户信息失败"),
    SET_GROUP_ADMIN_ERROR("10024", "设置群组管理员失败"),
    REMOVE_GROUP_ADMIN_ERROR("10025", "移除群组管理员失败"),
    GET_GROUP_INFO_ERROR("10026", "获取群组信息失败"),
    GET_CHATROOM_INFO_ERROR("10027", "获取聊天室信息失败"),


    CREATE_GROUP_ERROR("20010", "创建群组失败"),
    GROUP_NOT_FOUND("20011", "群组没有找到"),
    GROUP_COUNT_EXCEED("20012", "群人数已经超限"),
    JOIN_GROUP_ERROR("20013", "加入群组失败"),
    KICK_MEMBER_ERROR("20014", "踢人失败"),
    REMOVE_GROUP_ERROR("20015", "解散群组失败"),
    QUERY_GROUP_ERROR("20016", "查询群组失败"),
    MEMBER_NOT_FOUND("20017", "没有该成员"),
    GROUP_MSG_ERROR("20018", "发送群组消息失败"),
    LEAVE_GROUP_ERROR("20019", "退群失败"),
    ROOM_CLOSED_ERROR("20020", "房间已经关闭"),
    HAS_BEEN_BLOCKED("20021", "已经被拉黑，无法进入房间"),
    OWNER_LEAVE_GROUP("20022", "群主不能退出群"),

    USER_STREAM_EXISTS("300010", "该用户已经创建流"),
    STREAM_SUPPLIER_NOT_FOUND("300011", "流供应商没有找到"),
    STREAM_PUSH_FORBID("300012", "已被禁流"),
    STREAM_ERROR("300013", "流操作失败"),
    STREAM_NOT_FOUND("300013", "流id不存在"),
    NOT_SUPPORT("300014", "功能暂不支持"),
    MIX_STREAM_ERROR("300015", "混流失败"),
    STREAM_CHANGE_TYPE_ERROR("300016","上报流改变类型错误"),
    PARSE_STREAM_ERROR("300017","解析流失败"),
    MIX_STOP_STREAM_ERROR("300016", "取消混流失败"),
    NOT_HAVE_USABLE_SUPPLIER("300018", "无可用供应商"),

    PRODUCT_NOT_FOUND("400010", "没有该产品"),
    PRODUCT_IM_MODEL_ERROR("400011", "imModule错误"),
    IM_CONFIG_ERROR("400013", "im配置错误'"),
    SUPPLIER_NOT_FOUND("400014", "没有该供应商"),

    BALANCE_NOT_ENOUGH("300100", "账户钻石不足"),
    NOT_FREE_GIFT("300102", "该礼物不是免费礼物"),
    IS_FREE_GIFT("300103", "该礼物不是付费礼物"),
    GIFT_TYPE_ERROR("300104","礼物类型错误"),
    GIFT_PRICE_ERROR("300105","礼物价格错误"),
    FREE_GIFT_IN_TIME("300106", "免费礼物还在倒计时中,请耐心等候..."),
    INVALID_GIFT("300107", "礼物已下架，换一个吧！"),
    EXPIRED_GIFT("300108", "礼物已过期，请重新打开礼物面板刷新"),
    GIFT_ACTIVITY_END("300109", "该礼物的活动已结束"),
    PAY_ERROR("300101","支付失败"),
    USER_NOT_EXISTS("300110", "用户不存在"),
    USER_STATUS_ERROR("300111","用户状态异常"),
    Gift_NOT_EXISTS("300112", "礼物不存在"),
    Gift_CURRENT_TYPE_ERROR("300113", "礼物支付类型错误"),
    TO_UID_NOT_IN_CHATROOM("300114","被打赏人不在聊天室"),

    NO_GAME_READY_START("500010", "当前没有游戏准备开始"),
    GAME_NUM_ENOUGH("500011", "游戏人数已满"),
    GAME_CANCELED("500012", "对方已取消游戏"),
    GAME_NOT_FOUND("500013", "游戏没有找到"),
    GAME_NOT_FOUND_NOT_NOTIFY("500013", ""),
    GAME_NOT_TEAMING_STATUS_ERROR("500014", "游戏不在组队中"),
    GAME_INVITE_ERROR("500015", "麦位已满,暂无法邀请用户上麦游戏"),
    GAME_END_ERROR("500016", "游戏已经结束"),
    GAME_MIC_ERROR("500017", "麦位已满"),
    CALL_GAME_ERROR("500018", "调用游戏失败"),
    GAME_LIVING_ERROR("500019", "当前房间游戏中，结束后才可发起新游戏"),
    START_GAME_ERROR("500020", "发起游戏失败"),
    INVOKE_EXCEPTION("99999", "调用异常"),

    /**
     * web rtc
     */
    MULTI_MEDIA_FAILED_CUR_ONLINE("300400","通话正在进行,请结束当前通话"),
    MULTI_MEDIA_FAILED_TARGET_ONLINE("300401","对方正在通话,请稍后重试"),
    MULTI_MEDIA_FAILED_CUR_VIDEO_TYPE("300402","当前模式正是视频模式"),
    MULTI_MEDIA_FAILED_CUR_VERSION_NOT_SUPPORT("300403","对方版本不支持"),
    MULTI_MEDIA_FAILED_CUR_USER_NOT_SUPPORT("300404","当前用户不支持该功能"),
    MULTI_MEDIA_FAILED_CUR_MEDIA_NOT_EXIST("300405","当前通话不存在"),
    MULTI_MEDIA_FAILED_CUR_USER_NOT_POWER("300406","当前用户没有权限"),
    MULTI_MEDIA_FAILED_CUR_MEDIA_OPERATE_NOT_EXIST("300407","当前操作不存在"),
    MULTI_MEDIA_FAILED_TARGET_USER_NOT_ONLINE("300408","对方暂时未在线"),

    ;

    private String code;
    private String message;

    ExceptionCode(String code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Response getFailResponse(){
        return Response.fail(this.getCode(),this.getMessage());
    }
}
