package cn.bixin.sona.web.request;

import cn.bixin.sona.common.annotation.Description;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 客户端发送消息的格式
 *
 * @author qinwei
 */
@Getter
@Setter
public class MessageRequest implements Serializable {

    private static final long serialVersionUID = 3755067007595126891L;

    @Description("房间id")
    private Long roomId;

    @Description("uid")
    private Long uid;

    @Description("消息优先级")
    private int priority;

    @Description("消息内容")
    private String content;

    @Description("消息格式")
    private int msgFormat;

    @Description("消息id")
    private String messageId;

    @Description("是否需要保存")
    private boolean needToSave;

    @Description("业务消息类型")
    private String msgType;

}
