package cn.bixin.sona.console.domain.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Data
@Document(indexName = "mercury_report_log")
public class MercuryReportLog implements Serializable {
    @Id
    private String id;
    private Long sendTime;
    private String common;
    private String desc;
    private String details;
    private String ip;
    private String model;
    private String network;
    private String osVer;
    private String platform;
    private String type;
    private String uid;
}
