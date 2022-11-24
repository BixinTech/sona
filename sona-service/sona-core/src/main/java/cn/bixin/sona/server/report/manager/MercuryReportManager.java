package cn.bixin.sona.server.report.manager;

import cn.bixin.sona.api.report.request.MercuryReportRequest;
import cn.bixin.sona.server.mq.KafkaSender;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Component
public class MercuryReportManager {
    @Resource
    private KafkaSender kafkaSender;

    private static final String TOPIC_MERCURY_CLIENT_LOG = "TOPIC-MERCURY_CLIENT_LOG";

    public Boolean report(MercuryReportRequest request) {
        if (Objects.isNull(request)) {
            return false;
        }
        kafkaSender.send(TOPIC_MERCURY_CLIENT_LOG, JSON.toJSONString(request));
        return true;
    }
}
