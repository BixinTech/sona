package cn.bixin.sona.gateway.concurrent.counter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ctrip.framework.apollo.ConfigService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qinwei
 * <p>
 * 当房间5分钟内没有任何消息，清除掉对应的时间窗口计数
 */
@Component
public class CounterService {

    private static final String APOLLO_KEY_THRESHOLD = "counter.threshold.config";
    private static final String APOLLO_KEY_DURATION = "counter.duration.config";

    private static final Cache<String, TimeSlidingWindow> CAFFEINE = Caffeine.newBuilder().initialCapacity(64).expireAfterAccess(Duration.ofMinutes(5)).build();

    private static Map<String, Integer> thresholdConfig = new HashMap<>();

    private static TimeSlidingWindow getSlidingWindow(String name) {
        return CAFFEINE.get(name, s -> new TimeSlidingWindow(thresholdConfig.getOrDefault(s, 8)));
    }

    public static void increment(String name) {
        getSlidingWindow(name).increment();
    }

    public static boolean compute(String name) {
        return getSlidingWindow(name).exceedThreshold();
    }

    @PostConstruct
    public void init() {
        updateConfig(ConfigService.getAppConfig().getProperty(APOLLO_KEY_THRESHOLD, "{}"));
        ConfigService.getAppConfig().addChangeListener(changeEvent -> {
            if (changeEvent.isChanged(APOLLO_KEY_THRESHOLD)) {
                updateConfig(changeEvent.getChange(APOLLO_KEY_THRESHOLD).getNewValue());
            }
            if (changeEvent.isChanged(APOLLO_KEY_DURATION)) {
                String newValue = changeEvent.getChange(APOLLO_KEY_DURATION).getNewValue();
                CAFFEINE.policy().expireAfterAccess().ifPresent(expiration -> expiration.setExpiresAfter(Duration.ofMinutes(Long.parseLong(newValue))));
            }
        });
    }

    private static void updateConfig(String config) {
        Map<String, Integer> map = JSON.parseObject(config, new TypeReference<Map<String, Integer>>() {
        });
        if (map != null) {
            thresholdConfig = map;
        }
    }
}
