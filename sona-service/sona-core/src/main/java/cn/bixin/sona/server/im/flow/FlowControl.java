package cn.bixin.sona.server.im.flow;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author qinwei
 */
public class FlowControl {

    private static final Logger log = LoggerFactory.getLogger(FlowControl.class);

    private final StringRedisTemplate redisTemplate;

    private final RedisScript<Long> script;

    public FlowControl(StringRedisTemplate redisTemplate, RedisScript<Long> script) {
        this.redisTemplate = redisTemplate;
        this.script = script;
    }

    public FlowStrategy throttle(String key, FlowConfig config) {
        return Optional.ofNullable(config)
                .filter(conf -> execute(key, conf) != 1)
                .map(conf -> FlowStrategy.REFUSE)
                .orElse(FlowStrategy.PASS);
    }

    private Long execute(String key, FlowConfig config) {
        Transaction t = Cat.newTransaction("FLOW_CONTROL", "lua");
        long second = Instant.now().getEpochSecond();
        try {
            t.setSuccessStatus();
            return this.redisTemplate.execute(this.script,
                    getKeys(key),
                    String.valueOf(config.getCapacity()),
                    String.valueOf(config.getHighCapacity()),
                    String.valueOf(second),
                    String.valueOf(config.getDeduct()),
                    String.valueOf(config.getRequest()));
        } catch (Exception e) {
            log.error("flow control error .", e);
            t.setStatus(e);
            return 1L;
        } finally {
            t.complete();
        }
    }

    /**
     * redis cluster 执行lua脚本时，必须保证 keys 都在同一个slot中，
     * 可以用{roomId}.tokens  来保证同一个roomId 的keys都会被哈希到同一个slot中
     */
    private List<String> getKeys(String key) {
        return Arrays.asList(key + ".tokens", key + ".timestamp", key + ".timestamp.h");
    }

}
