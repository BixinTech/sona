CREATE TABLE `t_mix_config` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `room_id` bigint(64) NOT NULL DEFAULT '0' COMMENT '房间id',
  `uid` varchar(32) NOT NULL DEFAULT '' COMMENT '用户uid',
  `width` int(11) NOT NULL DEFAULT '1' COMMENT '画布宽–视频输出分辨率宽,纯音频混流的时候,推荐填写1',
  `height` int(11) NOT NULL DEFAULT '1' COMMENT '画布高–视频输出分辨率高,纯音频混流的时候,推荐填写1',
  `tops` int(11) NOT NULL DEFAULT '0' COMMENT '上边框,纯音频混流的时候,推荐填写0',
  `bottom` int(11) NOT NULL DEFAULT '1' COMMENT '下边框,纯音频混流的时候,推荐填写1',
  `lefts` int(11) NOT NULL DEFAULT '0' COMMENT '左边框,纯音频混流的时候,推荐填写0',
  `rights` int(11) NOT NULL DEFAULT '1' COMMENT '右边框,纯音频混流的时候,推荐填写1',
  `bitrate` int(11) NOT NULL DEFAULT '1' COMMENT '视频码率,纯音频混流的时候,推荐填写1',
  `fps` int(11) NOT NULL DEFAULT '1' COMMENT '视频帧率,纯音频混流的时候,推荐填写1',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '记录状态 1: 有效 0: 无效',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_room_id` (`room_id`),
  KEY `idx_uid` (`uid`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_update_time` (`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='房间混流配置表';

CREATE TABLE `t_mix_stream` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `room_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '房间ID',
  `stream_id` varchar(128) NOT NULL DEFAULT '' COMMENT '流ID',
  `source` tinyint(2) NOT NULL DEFAULT '1' COMMENT '来源 1:zego 2:tecent',
  `rtmp_url` varchar(1023) NOT NULL DEFAULT '' COMMENT 'rtmp_url',
  `hls_url` varchar(1023) NOT NULL DEFAULT '' COMMENT 'hls_url',
  `hdl_url` varchar(1023) NOT NULL DEFAULT '' COMMENT 'hdl_url',
  `input_stream` varchar(2056) NOT NULL DEFAULT '' COMMENT '输入流',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '1-打开 0-关闭',
  `begin_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据更新时间戳',
  `replay_url` varchar(1023) NOT NULL DEFAULT '' COMMENT 'rtmp_url',
  PRIMARY KEY (`id`),
  KEY `idx_room_id_time` (`room_id`,`create_time`),
  KEY `idx_stream_time` (`stream_id`,`create_time`),
  KEY `ix_create_time` (`create_time`),
  KEY `ix_stream_source_status_end_time` (`stream_id`,`source`,`status`,`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='sona混流记录表';

CREATE TABLE `t_product_config` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `product_code` varchar(32) NOT NULL DEFAULT '' COMMENT '产品码',
  `short_code` varchar(16) NOT NULL COMMENT '产品短码',
  `im_module` varchar(16) NOT NULL DEFAULT '' COMMENT 'im模块 CHATROOM=聊天室 GROUP=群组',
  `stream_supplier` varchar(16) NOT NULL DEFAULT '' COMMENT '流供应商',
  `type` varchar(16) NOT NULL DEFAULT '' COMMENT '流类型 AUDIO=音频流 VIDEO=视频流',
  `push_mode` varchar(16) NOT NULL DEFAULT '' COMMENT '推流模式',
  `pull_mode` varchar(16) NOT NULL DEFAULT '' COMMENT '拉流模式',
  `enter_notify_switch` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否下发进房消息',
  `check_admin` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否校验管理员 0=否 1=是',
  `need_replay` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否需要回放功能 0=否 1=是',
  `client_type` tinyint(4) NOT NULL DEFAULT '2' COMMENT '客户端类型 1=weblink 2=commonlink 3=wechatlink',
  `bitrate` int(11) NOT NULL DEFAULT '96000' COMMENT '播放器码率',
  `im_send_type` tinyint(4) DEFAULT '1' COMMENT '短连 1 长连2',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `product_code` (`product_code`),
  UNIQUE KEY `short_code` (`short_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品配置表';

CREATE TABLE `t_room` (
  `room_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '房间ID',
  `name` varchar(200) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '房间名',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-关闭，1-打开',
  `password` varchar(200) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '房间密码',
  `uid` varchar(32) NOT NULL DEFAULT '' COMMENT '用户uid',
  `product_code` varchar(64) NOT NULL DEFAULT '' COMMENT '产品码',
  `im_module` varchar(16) NOT NULL DEFAULT '' COMMENT 'im模块 CHATROOM=聊天室 GROUP=群组',
  `ext` varchar(1024) NOT NULL DEFAULT '' COMMENT 'ext',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据更新时间戳',
  PRIMARY KEY (`room_id`),
  KEY `idx_biz_room_id_product_code` (`product_code`),
  KEY `idx_product_code_status` (`product_code`,`status`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='sona聊天室房间表';

CREATE TABLE `t_room_config` (
  `room_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '房间ID',
  `product_code` varchar(32) NOT NULL DEFAULT '' COMMENT '产品码',
  `im_module` varchar(16) NOT NULL DEFAULT '' COMMENT 'im模块 CHATROOM=聊天室 GROUP=群组',
  `stream_supplier` varchar(16) NOT NULL DEFAULT '' COMMENT '流供应商',
  `type` varchar(16) NOT NULL DEFAULT '' COMMENT '流类型 AUDIO=音频 VIDEO=视频',
  `push_mode` varchar(16) NOT NULL DEFAULT '' COMMENT '推流模式',
  `pull_mode` varchar(16) NOT NULL DEFAULT '' COMMENT '拉流模式',
  `enter_notify_switch` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否下发进房消息',
  `check_admin` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否校验管理员 0=否 1=是',
  `need_replay` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否需要回放功能 0=否 1=是',
  `client_type` tinyint(4) NOT NULL DEFAULT '2' COMMENT '客户端类型 1=weblink 2=commonlink 3=wechatlink',
  `bitrate` int(11) NOT NULL DEFAULT '96000' COMMENT '播放器码率',
  `im_send_type` tinyint(4) DEFAULT '1' COMMENT '1=短连 2=长连',
  `short_code` varchar(16) NOT NULL DEFAULT '' COMMENT '产品短码',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`room_id`),
  KEY `idx_product_code` (`product_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='房间配置表';

CREATE TABLE `t_room_group_member` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `room_id` bigint(32) NOT NULL DEFAULT '0' COMMENT '房间id',
  `product_code` varchar(32) NOT NULL DEFAULT '' COMMENT '产品码',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-离开，1-在线',
  `uid` bigint(32) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_room_id` (`room_id`),
  KEY `idx_uid_product_code_status` (`uid`,`product_code`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='群组成员表';

CREATE TABLE `t_room_management_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `uid` varchar(32) NOT NULL DEFAULT '' COMMENT '用户uid',
  `room_id` bigint(32) NOT NULL DEFAULT '0' COMMENT '房间id',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-失效，1-正常',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1-拉黑 2-禁言 4-管理员 5-房主',
  `expire_time` datetime NOT NULL DEFAULT '2099-12-31 23:59:59' COMMENT '失效时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据更新时间戳',
  PRIMARY KEY (`id`),
  KEY `idx_room_id_uid` (`room_id`,`uid`),
  KEY `idx_uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='房间管理员信息表';

CREATE TABLE `t_room_management_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `room_id` bigint(32) NOT NULL DEFAULT '0' COMMENT '房间id',
  `target_uid` varchar(32) NOT NULL DEFAULT '' COMMENT '被操作用户uid',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '操作类型: 1拉黑、2禁言、3踢出',
  `operate` tinyint(1) NOT NULL DEFAULT '0' COMMENT '操作类型: 1设置 0取消',
  `operator` varchar(32) NOT NULL DEFAULT '' COMMENT '操作人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据更新时间戳',
  PRIMARY KEY (`id`),
  KEY `idx_room_id` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='房间管理记录表';

CREATE TABLE `t_room_operate_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `room_id` bigint(32) NOT NULL DEFAULT '0' COMMENT '房间id',
  `type` varchar(10) NOT NULL DEFAULT '' COMMENT '房间类型: CHATROOM, GROUP',
  `operate` varchar(32) NOT NULL DEFAULT '' COMMENT '房间操作: CREATE, OPEN, CLOSE',
  `operator` varchar(32) NOT NULL COMMENT '操作人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据更新时间戳',
  PRIMARY KEY (`id`),
  KEY `idx_room_id` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='房间操作记录表';

CREATE TABLE `t_room_stream` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `stream_id` varchar(255) NOT NULL COMMENT '流ID',
  `product_code` varchar(16) NOT NULL DEFAULT '' COMMENT '产品码',
  `room_id` bigint(32) NOT NULL DEFAULT '0' COMMENT '房间ID',
  `biz_room_id` varchar(512) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:打开 0:关闭 -1:未开始',
  `uid` bigint(32) NOT NULL DEFAULT '0' COMMENT '用户UID',
  `source` int(2) NOT NULL DEFAULT '1' COMMENT '来源 1:zego 2:tecent',
  `rtmp_url` varchar(1023) NOT NULL DEFAULT '' COMMENT 'rtmp_url',
  `hls_url` varchar(1023) NOT NULL DEFAULT '' COMMENT 'hls_url',
  `hdl_url` varchar(1023) NOT NULL DEFAULT '' COMMENT 'hdl_url',
  `pic_url` varchar(255) NOT NULL DEFAULT '' COMMENT 'pic_url',
  `replay_url` varchar(255) NOT NULL DEFAULT '' COMMENT 'replay_url',
  `begin_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `close_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '关闭时间',
  `end_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
  `close_type` int(2) NOT NULL DEFAULT '-1' COMMENT '0-正常关闭 1-后台超时关闭 2-同一主播直播关闭之前 没有关闭的流',
  `err_msg` varchar(256) NOT NULL DEFAULT '' COMMENT '错误描述',
  `ext` varchar(1024) NOT NULL DEFAULT '' COMMENT '扩展信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_stream_id` (`stream_id`),
  KEY `idx_room_id_status_uid` (`room_id`,`status`,`uid`),
  KEY `idx_room_id_status_source` (`room_id`,`status`,`source`),
  KEY `idx_uid_status` (`uid`,`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='sona音视频流记录表';

CREATE TABLE `t_switch_batch` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `switch_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态：0，1-手动，2-自动',
  `stream_supplier` varchar(16) NOT NULL DEFAULT '' COMMENT '流供应商',
  `pull_mode` varchar(16) NOT NULL DEFAULT '' COMMENT '拉流模式',
  `file_url` varchar(100) NOT NULL DEFAULT '' COMMENT '文件地址',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态：0-进行，1-完成，2-失败',
  `operator` varchar(32) NOT NULL DEFAULT '' COMMENT '操作人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='热切批次信息表';

CREATE TABLE `t_switch_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `batch_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '批次Id',
  `room_id` bigint(64) NOT NULL DEFAULT '0' COMMENT '房间id',
  `stream_supplier` varchar(16) NOT NULL DEFAULT '' COMMENT '流供应商',
  `pull_mode` varchar(16) NOT NULL DEFAULT '' COMMENT '拉流模式',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态：0-进行，1-完成，2-失败',
  `operator` varchar(32) NOT NULL DEFAULT '' COMMENT '操作人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='热切明细信息表';
