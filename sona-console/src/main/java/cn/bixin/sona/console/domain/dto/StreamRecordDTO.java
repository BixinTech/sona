package cn.bixin.sona.console.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class StreamRecordDTO implements Serializable {

    private Long uid;

    private String streamId;

    private int status;

    private String replayUrl;

    private Date beginTime;

    private Date endTime;

    private int source;

    private Long roomId;

    private Long id;

    private int limit;

    private Long anchor;

}
