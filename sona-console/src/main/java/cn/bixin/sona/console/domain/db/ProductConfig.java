package cn.bixin.sona.console.domain.db;

import java.util.Date;
import lombok.Data;

/**
    * 产品配置表
    */
@Data
public class ProductConfig {
    /**
    * 主键ID
    */
    private Long id;

    /**
    * 产品码
    */
    private String productCode;

    /**
    * im模块 CHATROOM=聊天室 GROUP=群组
    */
    private String imModule;

    /**
    * 流供应商
    */
    private String streamSupplier;

    /**
    * 流类型 AUDIO=音频流 VIDEO=视频流
    */
    private String type;

    /**
    * 推流模式
    */
    private String pushMode;

    /**
    * 拉流模式
    */
    private String pullMode;

    /**
    * 是否下发进房消息
    */
    private Byte enterNotifySwitch;

    /**
    * 是否校验管理员 0=否 1=是
    */
    private Byte checkAdmin;

    /**
    * 是否需要回放功能 0=否 1=是
    */
    private Byte needReplay;

    /**
    * 客户端类型 1=weblink 2=commonlink 3=wechatlink
    */
    private Byte clientType;

    /**
    * 播放器码率
    */
    private Integer bitrate;

    /**
    * 短连 1 长连2
    */
    private Byte imSendType;

    /**
    * 产品短码
    */
    private String shortCode;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;
}