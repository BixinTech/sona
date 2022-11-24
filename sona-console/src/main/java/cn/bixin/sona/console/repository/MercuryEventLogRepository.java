package cn.bixin.sona.console.repository;

import cn.bixin.sona.console.domain.es.MercuryEventLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MercuryEventLogRepository extends ElasticsearchRepository<MercuryEventLog, String> {
}
