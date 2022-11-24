package cn.bixin.sona.console.service;

import cn.bixin.sona.console.domain.es.RoomImMsgLog;
import cn.bixin.sona.console.domain.req.RoomImMsgLogQuery;
import org.springframework.data.domain.Page;

public interface RoomImMsgLogService {
    RoomImMsgLog save(RoomImMsgLog roomImMsgLog);

    Page<RoomImMsgLog> pageQuery(RoomImMsgLogQuery query);
}
