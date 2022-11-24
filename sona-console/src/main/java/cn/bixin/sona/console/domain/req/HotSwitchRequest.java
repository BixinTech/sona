package cn.bixin.sona.console.domain.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class HotSwitchRequest {

    /**
     * 房间id
     */
    @NotBlank(message = "房间id不可为空!")
    private String roomIds;

    /**
     * 云商类型
     */
    private Integer switchType;

    /**
     * 登陆人
     */
    private String loginName;
}
