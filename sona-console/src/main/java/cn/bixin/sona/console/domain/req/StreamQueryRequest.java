package cn.bixin.sona.console.domain.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class StreamQueryRequest implements Serializable {

    /**
     * 流id
     */
    private String streamId;

    /**
     * 房间id
     */
    private String roomId;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 游标标识
     */
    private String anchor;

    /**
     * 限制返回结果数
     */
    private int limit;

}