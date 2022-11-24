package cn.bixin.sona.gateway.msg;

import lombok.Getter;
import lombok.Setter;

/**
 * 握手消息body
 *
 * @author qinwei
 */
@Getter
@Setter
public class HandShakeBody {

    /**
     * 设备id
     */
    private String d;

    /**
     * 平台 1: iOS；2: Android; 3: PC
     */
    private int p;

    /**
     * 系统版本号
     */
    private String sv;

    /**
     * 用户ID
     */
    private String u;

    /**
     * model
     */
    private String m;

    /**
     * type，通道类型。
     *
     * @see cn.bixin.sona.gateway.common.ChannelTypeEnum
     */
    private int t;

    /**
     * app是否在后台 0: 前台, 1: 后台
     */
    private int b;

    public HandShakeBody() {
    }
}
