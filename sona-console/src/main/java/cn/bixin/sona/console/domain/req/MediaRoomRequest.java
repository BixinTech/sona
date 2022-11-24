package cn.bixin.sona.console.domain.req;

import lombok.Data;

@Data
public class MediaRoomRequest {

    /**
     * 房间ids
     */
    private String roomIds;

    /**
     * 云商类型
     */
    private String cloudSupplierType;

    /**
     * 拉流类型
     */
    private String pullStreamType;
}
