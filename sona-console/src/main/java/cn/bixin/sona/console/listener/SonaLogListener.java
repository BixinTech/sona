package cn.bixin.sona.console.listener;

import cn.bixin.sona.console.domain.es.MercuryEventLog;
import cn.bixin.sona.console.domain.es.MercuryReportLog;
import cn.bixin.sona.console.domain.es.RoomImMsgLog;
import cn.bixin.sona.console.handler.LogHandler;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component
public class SonaLogListener {

    @Resource
    private LogHandler logHandler;

    /**
     * 房间消息全链路日志
     * @param record
     */
    @KafkaListener(topics = "TOPIC-ROOM-IM-MESSAGE-LOG", groupId = "TOPIC-ROOM-IM-MESSAGE-LOG_group")
    public void listenRoomImMsgLog(ConsumerRecord<String, String> record) {
        log.info("TOPIC-ROOM-IM-MESSAGE-LOG:{}", record.value());
        logHandler.handleRoomImMsgLog(JSON.parseObject(record.value(), RoomImMsgLog.class));
    }

    /**
     * 长链事件日志
     * @param record
     */
    @KafkaListener(topics = "TOPIC-MERCURY_EVENT_LOG", groupId = "TOPIC-MERCURY_EVENT_LOG_group")
    public void listenMercuryEventLog(ConsumerRecord<String, String> record) {
        logHandler.handleMercuryEventLog(JSON.parseObject(record.value(), MercuryEventLog.class));
    }

    /**
     * 客户端上报长连日志
     * @param record
     */
    @KafkaListener(topics = "TOPIC-MERCURY_CLIENT_LOG", groupId = "TOPIC-MERCURY_CLIENT_LOG_group")
    public void listenMercuryClientLog(ConsumerRecord<String, String> record) {
        logHandler.handleMercuryClientLog(JSON.parseObject(record.value(), MercuryReportLog.class));
    }

}
