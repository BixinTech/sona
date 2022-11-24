package cn.bixin.sona.console.service;

import cn.bixin.sona.console.domain.es.MercuryReportLog;
import cn.bixin.sona.console.domain.req.MercuryReportLogQuery;
import org.springframework.data.domain.Page;

public interface MercuryReportLogService {

    void save(MercuryReportLog mercuryReportLog);

    Page<MercuryReportLog> pageQuery(MercuryReportLogQuery query);
}
