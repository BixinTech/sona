package cn.bixin.sona.console.domain.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class SwitchBatchRecordDTO implements Serializable {
    private static final long serialVersionUID = -2096671673412479497L;

    private Long id;

    private Integer switchType;

    private String streamSupplier;

    private String pullMode;

    private String fileUrl;

    private Integer status;

    private String operator;

    private String createTime;

    private String updateTime;

}