package cn.bixin.sona.console.controller;

import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.console.domain.es.MercuryEventLog;
import cn.bixin.sona.console.domain.es.MercuryReportLog;
import cn.bixin.sona.console.domain.es.RoomImMsgLog;
import cn.bixin.sona.console.domain.req.MercuryEventLogQuery;
import cn.bixin.sona.console.domain.req.MercuryReportLogQuery;
import cn.bixin.sona.console.domain.req.RoomImMsgLogQuery;
import cn.bixin.sona.console.service.MercuryEventLogService;
import cn.bixin.sona.console.service.MercuryReportLogService;
import cn.bixin.sona.console.service.RoomImMsgLogService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@CrossOrigin
@RestController
public class SonaLogController {
    @Resource
    private MercuryEventLogService mercuryEventLogService;
    @Resource
    private MercuryReportLogService mercuryReportLogService;
    @Resource
    private RoomImMsgLogService roomImMsgLogService;

    /**
     * 长连事件上报日志查询
     * @return
     */
    @PostMapping("/mercury/event/log/query")
    public Response<Page<MercuryEventLog>> mercuryEventLogQuery(@RequestBody MercuryEventLogQuery query) {
        return Response.success(mercuryEventLogService.pageQuery(query));
    }

    /**
     * 长连客户端上报日志查询
     * @return
     */
    @PostMapping("/mercury/client/log/query")
    public Response<Page<MercuryReportLog>> mercuryClientLogQuery(@RequestBody MercuryReportLogQuery query) {
        return Response.success(mercuryReportLogService.pageQuery(query));
    }

    /**
     * 房间消息全链路日志查询
     * @return
     */
    @PostMapping("/room/im/msg/log/query")
    public Response<Page<RoomImMsgLog>> roomImMsgLogQuery(@RequestBody RoomImMsgLogQuery query) {
        return Response.success(roomImMsgLogService.pageQuery(query));
    }
}
