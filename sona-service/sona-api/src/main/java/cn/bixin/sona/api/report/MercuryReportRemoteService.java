package cn.bixin.sona.api.report;

import cn.bixin.sona.api.report.request.MercuryReportRequest;
import cn.bixin.sona.common.dto.Response;

public interface MercuryReportRemoteService {
    /**
     * 长链日志上报
     * @param request
     * @return
     */
    Response<Boolean> report(MercuryReportRequest request);
}
