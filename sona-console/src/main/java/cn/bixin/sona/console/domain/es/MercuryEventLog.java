package cn.bixin.sona.console.domain.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Data
@Document(indexName = "mercury_event_log")
public class MercuryEventLog implements Serializable {
    @Id
    private String id;
    private String uid;
    private String server;
    private String addr;
    private String type;
    private String device;
    private String event;
    private String content;
    private String cmd;
    private String header;
    private Long sendTime;
}