package cn.bixin.sona.gateway.config;

import cn.bixin.sona.common.rocket.TraceConsumerHook;
import cn.bixin.sona.common.rocket.TraceSendHook;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qinwei
 */
@Configuration
public class TraceConfiguration {

    @Bean
    public TraceConsumerHook traceConsumerHook() {
        return new TraceConsumerHook();
    }

    @Bean
    public TraceSendHook traceSendHook() {
        return new TraceSendHook();
    }

}
