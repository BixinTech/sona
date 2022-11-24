package cn.bixin.sona.console.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatroomStreamDTO implements Serializable {

    /**
     * 房间id
     */
    private String roomId;


    /**
     * 用户uid
     */
    private String uid;

    /**
     * 流状态 -1=初始化 0=关闭 1=打开
     */
    private int status;
    /**
     * 回放地址 录制地址
     */
    private String replayUrl;
    /**
     * 来源 ZEGO/TECENT 云商
     */
    private String source;
    /**
     * 流开始时间
     */
    private String beginTime;
    /**
     * 流结束时间
     */
    private String endTime;


}