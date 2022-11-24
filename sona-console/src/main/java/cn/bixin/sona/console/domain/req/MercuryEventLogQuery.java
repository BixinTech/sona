package cn.bixin.sona.console.domain.req;

import cn.bixin.sona.console.domain.es.MercuryEventLog;
import lombok.Data;

@Data
public class MercuryEventLogQuery extends MercuryEventLog {
    private Integer pageNo;
    private Integer pageSize;
    private Long fromTime;
    private Long toTime;

    public Integer getPageNo() {
        if (pageNo == null) {
            return 0;
        }
        return pageNo;
    }

    public Integer getPageSize() {
        if (pageSize == null) {
            return 50;
        }
        return pageSize;
    }

}
