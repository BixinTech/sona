package cn.bixin.sona.console.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MediaRoomInfoDTO implements Serializable {

    /**
     * 房间id
     */
    private String roomId;

    /**
     * 云商类型
     */
    private String cloudType;

    /**
     * 拉流模式
     */
    private String pullStreamType;

    /**
     * 流地址
     */
    private String streamUrl;
}
