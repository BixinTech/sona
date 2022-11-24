package cn.bixin.sona.console.domain.db;

import java.util.Date;
import lombok.Data;

/**
 * 热切明细信息表
 */
@Data
public class SwitchDetail {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 批次Id
     */
    private Long batchId;

    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 流供应商
     */
    private String streamSupplier;

    /**
     * 拉流模式
     */
    private String pullMode;

    /**
     * 状态：0-进行，1-完成，2-失败
     */
    private Integer status;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}