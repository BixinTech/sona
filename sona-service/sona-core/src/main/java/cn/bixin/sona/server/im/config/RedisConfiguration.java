package cn.bixin.sona.server.im.config;

import cn.bixin.sona.server.im.flow.FlowControl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @author qinwei
 */
@Configuration
public class RedisConfiguration {

    @Bean
    public RedisScript<Long> rateLimiterScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("META-INF/scripts/request_rate_limiter.lua")));
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    @Bean
    public FlowControl flowControl(StringRedisTemplate stringRedisTemplate, RedisScript<Long> rateLimiterScript) {
        return new FlowControl(stringRedisTemplate, rateLimiterScript);
    }
}
