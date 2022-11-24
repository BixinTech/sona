package cn.bixin.sona.console.handler;

import cn.bixin.sona.console.domain.es.MercuryEventLog;
import cn.bixin.sona.console.domain.es.MercuryReportLog;
import cn.bixin.sona.console.domain.es.RoomImMsgLog;
import cn.bixin.sona.console.service.MercuryEventLogService;
import cn.bixin.sona.console.service.MercuryReportLogService;
import cn.bixin.sona.console.service.RoomImMsgLogService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LogHandler {
    @Resource
    private RoomImMsgLogService roomImMsgLogService;
    @Resource
    private MercuryEventLogService mercuryEventLogService;
    @Resource
    private MercuryReportLogService mercuryReportLogService;

    public void handleRoomImMsgLog(RoomImMsgLog roomImMsgLog) {
        roomImMsgLogService.save(roomImMsgLog);
    }

    public void handleMercuryEventLog(MercuryEventLog mercuryEventLog) {
        mercuryEventLogService.save(mercuryEventLog);
    }

    public void handleMercuryClientLog(MercuryReportLog mercuryReportLog) {
        mercuryReportLogService.save(mercuryReportLog);
    }
}
