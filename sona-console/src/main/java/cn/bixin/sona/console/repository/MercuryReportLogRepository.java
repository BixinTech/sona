package cn.bixin.sona.console.repository;

import cn.bixin.sona.console.domain.es.MercuryReportLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MercuryReportLogRepository extends ElasticsearchRepository<MercuryReportLog, String> {
}
