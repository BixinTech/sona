package cn.bixin.sona.server.report.controller;

import cn.bixin.sona.api.report.MercuryReportRemoteService;
import cn.bixin.sona.api.report.request.MercuryReportRequest;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.server.report.manager.MercuryReportManager;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class MercuryReportController implements MercuryReportRemoteService {
    @Resource
    private MercuryReportManager mercuryReportManager;
    @Override
    public Response<Boolean> report(MercuryReportRequest request) {
        return Response.success(mercuryReportManager.report(request));
    }
}
