package cn.bixin.sona.console.domain.req;

import cn.bixin.sona.console.domain.es.MercuryReportLog;
import lombok.Data;

@Data
public class MercuryReportLogQuery extends MercuryReportLog {
    private Integer pageNo;
    private Integer pageSize;
    private String fromTime;
    private String toTime;

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
