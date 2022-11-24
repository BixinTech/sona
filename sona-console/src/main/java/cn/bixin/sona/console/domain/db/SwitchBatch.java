package cn.bixin.sona.console.domain.db;

import java.util.Date;
import lombok.Data;

/**
    * 热切批次信息表
    */
@Data
public class SwitchBatch {
    /**
    * 主键ID
    */
    private Long id;

    /**
    * 状态：0，1-手动，2-自动
    */
    private Integer switchType;

    /**
    * 流供应商
    */
    private String streamSupplier;

    /**
    * 拉流模式
    */
    private String pullMode;

    /**
    * 文件地址
    */
    private String fileUrl;

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