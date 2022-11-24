package cn.bixin.sona.web.response;

import java.util.List;

/**
 * @author yuanye
 * @date 2021/3/4 2:27 下午
 */
public class SupplierVideoStreamVO {

    /**
     * 描述
     */
    private String desc;

    /**
     * 流id
     */
    private String streamId;

    /**
     * 云商
     */
    private String supplier;

    /**
     * 拉流地址
     */
    private List<VideoPullStreamVO> pullUrls;
}
