package cn.bixin.sona.api.im.request;

import cn.bixin.sona.common.annotation.Description;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 群组消息
 *
 * @author qinwei
 */
public class GroupMessageRequest extends MessageRequest implements Serializable {

    private static final long serialVersionUID = 61379033154232653L;

    @NotNull
    @Description("群组id")
    private Long groupId;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
