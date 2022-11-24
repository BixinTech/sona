package cn.bixin.sona.console.domain.db;

import java.util.Date;
import lombok.Data;

/**
    * 聊天室信息主表
    */
@Data
public class Chatroom {
    /**
    * 唯一主键
    */
    private Long pid;

    private String id;

    /**
    * 房主user_id
    */
    private String userId;

    /**
    * 房间名
    */
    private String roomTitle;

    /**
    * 房间tag
    */
    private String roomTag;

    /**
    * 城市
    */
    private String cityName;

    /**
    * 模板，1=派单-废弃 2=交友 3=陪玩 4=电台 20=个人
    */
    private Boolean templet;

    /**
    * 云信聊天室id
    */
    private Integer chatRoomId;

    /**
    * 聊天室状态， 0=冻结 1=正常 2=关闭 9=初始化插入
    */
    private Boolean status;

    /**
    * 房间号
    */
    private Integer roomNo;

    /**
    * 品牌名
    */
    private String brandName;

    /**
    * 主题边框 0=关闭，1=开启
    */
    private Boolean hasThemeBorder;

    /**
    * 人数限制
    */
    private Integer peopleLimit;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 数据更新时间戳
    */
    private Date updateTimestamp;

    /**
    * 10=比心,20=鱼耳
    */
    private Integer appId;

    /**
    * 房主token
    */
    private String token;

    /**
    * 房主创建云信房间的accId
    */
    private String accid;

    /**
    * 聊天室展示端(10比心，20鱼耳，0双端)
    */
    private Integer showType;

    /**
    * uid
    */
    private Long uid;

    /**
    *  房间密码
    */
    private String password;

    /**
    *  remote房间id
    */
    private String remoteChatroomId;

    /**
    * sona_room_id
    */
    private Long sonaRoomId;

    /**
    * pattern_id
    */
    private Integer patternId;

    /**
    * 0: 公共房间 1：个人房间 2:飞鱼房间 3:开黑房间
    */
    private Integer roomType;

    /**
    * 业务线id
    */
    private Integer sourceId;

    /**
    * 来源平台，ios,pc..
    */
    private String platform;

    /**
    * 玩法code
    */
    private String functionCode;
}