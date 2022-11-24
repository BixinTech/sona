package cn.bixin.sona.web.response;

import lombok.Data;

/**
 * 视频拉流模型
 * @author yuanye
 * @date 2021/3/4 2:35 下午
 */
@Data
public class VideoPullStreamVO {

    /**
     * 流id
     */
    private String streamId;
    /**
     * 拉流地址
     */
    private String pullUrl;
    /**
     * 清晰度
     */
    private String definition;
    /**
     * 清晰度id
     */
    private Integer definitionId;
    /**
     * 协议
     */
    private String protocol;
    /**
     * 是否默认
     */
    private Boolean isDefault;
}
