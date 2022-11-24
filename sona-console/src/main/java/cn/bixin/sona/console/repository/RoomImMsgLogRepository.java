package cn.bixin.sona.console.repository;

import cn.bixin.sona.console.domain.es.RoomImMsgLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomImMsgLogRepository extends ElasticsearchRepository<RoomImMsgLog, String> {
}
