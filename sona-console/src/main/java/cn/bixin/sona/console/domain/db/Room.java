package cn.bixin.sona.console.domain.db;

import java.util.Date;
import lombok.Data;

/**
    * sona聊天室房间表
    */
@Data
public class Room {
    /**
    * 房间ID
    */
    private Long roomId;

    /**
    * 房间名
    */
    private String name;

    /**
    * 状态：0-关闭，1-打开
    */
    private Boolean status;

    /**
    * 房间密码
    */
    private String password;

    /**
    * 用户uid
    */
    private String uid;

    /**
    * 产品码
    */
    private String productCode;

    /**
    * im模块 CHATROOM=聊天室 GROUP=群组
    */
    private String imModule;

    /**
    * ext
    */
    private String ext;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 数据更新时间戳
    */
    private Date updateTime;
}