package cn.bixin.sona.gateway.util;

import cn.bixin.sona.common.spring.SpringApplicationContext;
import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.channel.support.ChannelAttrs;
import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.common.Header;
import cn.bixin.sona.gateway.concurrent.counter.SystemClock;
import cn.bixin.sona.gateway.mq.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qinwei
 */
@Slf4j
public class EventRecordLog {

    private static final String TOPIC_MERCURY_EVENT_LOG = "TOPIC-MERCURY_EVENT_LOG";

    public static void logEvent(NettyChannel channel, String event, String desc) {
        logEvent(channel, event, null, desc);
    }

    public static void logEvent(NettyChannel channel, String event, AccessMessage message, String desc) {
        Map<String, Object> map = new HashMap<>(16);
        ChannelAttrs attrs = channel.getAttrs();
        map.put("uid", channel.getUid());
        map.put("server", NetUtil.LOCAL_IP_ADDR);
        map.put("addr", channel.getRemoteAddress().toString());
        map.put("type", attrs.getChannelType());
        map.put("device", attrs.getDeviceId());
        map.put("event", event);
        map.put("content", desc);
        if (message != null) {
            map.put("cmd", message.getCmd());
            List<Header> headers = message.getHeaders();
            if (!CollectionUtils.isEmpty(headers)) {
                map.put("header", headers.toString());
            }
        }
        map.put("sendTime", SystemClock.currentTimeMillis());
//        log.info("mercury_event : {}", JSON.toJSONString(map));
        SpringApplicationContext.getBean(KafkaSender.class).send(TOPIC_MERCURY_EVENT_LOG, map);
    }

}
