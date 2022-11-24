package cn.bixin.sona.gateway.config;

import cn.bixin.sona.gateway.channel.support.AccessFilter;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author qinwei
 */
@Component
@Getter
@Setter
public class ApolloConfiguration {

    /**
     * server端需要重启时，对发送的close消息进行平滑限流，每秒最多发出X条close消息
     */
    @Value("${close.msg.throttling:750}")
    private int closeMsgThrottling;

    /**
     * 开始发送close消息后，server会等待连接全部被关闭。但此过程如果超过X秒，则不再等待，直接调用shutdownGracefully。
     */
    @Value("${close.msg.max.wait.seconds:40}")
    private int closeMsgMaxWaitSeconds;

    /**
     * 连接建立后X秒内不握手的连接，会被强制断开
     */
    @Value("${handshake.wait.seconds:5}")
    private int handshakeWaitSeconds;

    /**
     * X秒内没有读到过新数据的连接，认为是僵死连接，会被强制断开
     */
    @Value("${channel.idle.seconds:280}")
    private int channelIdleSeconds;

    /**
     * X秒内没有读到过新数据，且在前台状态的连接，服务端主动发送探测消息
     */
    @Value("${probe.idle.seconds:130}")
    private int probeIdleSeconds;

    /**
     * 发送探测消息后X秒内没有收到回复的连接，会被强制断开
     */
    @Value("${probe.wait.seconds:4}")
    private int probeWaitSeconds;

    /**
     * 持续时间超过X小时的连接，定期断开，以免redis数据过期失效，导致其他错误。设为-1关闭此功能
     */
    @Value("${long.lasting.close.hours:144}")
    private int longLastingCloseHours;

    @Value("${room.message.async:false}")
    private boolean roomMessageAsync;

    @ApolloConfig
    private Config config;

    private static final String KEY_IP_RULES = "access.filter.rules";

    @PostConstruct
    public void init() {
        AccessFilter.INSTANCE.updateRules(config.getProperty(KEY_IP_RULES, "[]"));
    }

    @ApolloConfigChangeListener
    public void onChange(ConfigChangeEvent changeEvent) {
        if (changeEvent.isChanged(KEY_IP_RULES)) {
            AccessFilter.INSTANCE.updateRules(config.getProperty(KEY_IP_RULES, "[]"));
        }
    }

}
