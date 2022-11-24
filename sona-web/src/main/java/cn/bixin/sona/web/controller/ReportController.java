package cn.bixin.sona.web.controller;

import cn.bixin.sona.api.report.MercuryReportRemoteService;
import cn.bixin.sona.api.report.request.MercuryReportRequest;
import cn.bixin.sona.common.dto.Response;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sona/report")
public class ReportController {
    @DubboReference
    private MercuryReportRemoteService mercuryReportRemoteService;

    @PostMapping("/mercury")
    public Response<Boolean> report(@RequestBody MercuryReportRequest request) {
        return mercuryReportRemoteService.report(request);
    }


}
