package cn.bixin.sona.console.domain.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Data
@Document(indexName = "room_im_msg_log")
public class RoomImMsgLog implements Serializable {
    @Id
    private String id;
    private String messageId;
    private String roomId;
    private String msgType;
    private String productCode;
    private String priority;
    private String uid;
    private String content;
    private String toUid;
    private Long sendTime;
}