package cn.bixin.sona.console.service;

import cn.bixin.sona.console.domain.es.MercuryEventLog;
import cn.bixin.sona.console.domain.req.MercuryEventLogQuery;
import org.springframework.data.domain.Page;

public interface MercuryEventLogService {
    void save(MercuryEventLog mercuryEventLog);

    Page<MercuryEventLog> pageQuery(MercuryEventLogQuery query);
}
