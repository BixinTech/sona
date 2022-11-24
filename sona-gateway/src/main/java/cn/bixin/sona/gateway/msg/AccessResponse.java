package cn.bixin.sona.gateway.msg;

import lombok.Getter;
import lombok.Setter;

/**
 * @author qinwei
 */
@Getter
@Setter
public class AccessResponse {

    public static final AccessResponse SUCCESS = new AccessResponse(0, "success");

    public static final AccessResponse ROOM_HEADER_NOT_FOUND = new AccessResponse(8010, "Room header not found");

    public static final AccessResponse INVALID_ROOM_NAME = new AccessResponse(8010, "Invalid room name");

    public static final AccessResponse NOT_IN_ROOM_WHEN_SEND = new AccessResponse(8010, "Not in room when send");

    public static final AccessResponse EMPTY_UID = new AccessResponse(8010, "Empty uid");

    public static final AccessResponse VISITOR_SEND = new AccessResponse(8010, "Visitor can't send message");

    public static final AccessResponse INVALID_UID = new AccessResponse(8010, "Invalid uid");

    public static final AccessResponse INVALID_DATA = new AccessResponse(8010, "Invalid data");

    public static final AccessResponse EMPTY_BODY = new AccessResponse(8010, "Empty body");

    public static final AccessResponse ACCESS_FAIL = new AccessResponse(404, "Access fail,error param");

    /**
     * 状态码 0：成功 其他：失败
     */
    private int c;

    /**
     * 响应数据
     */
    private String d;

    public AccessResponse() {

    }

    public AccessResponse(int c, String d) {
        this.c = c;
        this.d = d;
    }

}
