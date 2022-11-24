package cn.bixin.sona.server.im.config;

import cn.bixin.sona.server.im.flow.FlowConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ctrip.framework.apollo.ConfigService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author qinwei
 */
@Component
public class ApolloConfig {

    private static final String APOLLO_KEY_MESSAGE_FLOW_CONFIG = "message.flow.config";

    private static final String APOLLO_KEY_MESSAGE_DELAY_CONFIG = "message.delay.config";

    private Map<String, FlowConfig> flowConfig = new HashMap<>();

    private Map<String, Long> delayConfig = new HashMap<>();

    @PostConstruct
    public void init() {
        updateFlowConfig(ConfigService.getAppConfig().getProperty(APOLLO_KEY_MESSAGE_FLOW_CONFIG, "{\"HIGH\":{\"capacity\":0,\"highCapacity\":30,\"request\":1,\"deduct\":1},\"MEDIUM_HIGH\":{\"capacity\":60,\"highCapacity\":30,\"request\":30,\"deduct\":1},\"MEDIUM\":{\"capacity\":60,\"highCapacity\":30,\"request\":30,\"deduct\":1},\"LOW\":{\"capacity\":60,\"highCapacity\":30,\"request\":60,\"deduct\":1}}"));
        updateDelayConfig(ConfigService.getAppConfig().getProperty(APOLLO_KEY_MESSAGE_DELAY_CONFIG, "{\"MEDIUM_HIGH\":30000,\"MEDIUM\":5000,\"LOW\":1000}"));

        ConfigService.getAppConfig().addChangeListener(changeEvent -> {
            if (changeEvent.isChanged(APOLLO_KEY_MESSAGE_FLOW_CONFIG)) {
                updateFlowConfig(changeEvent.getChange(APOLLO_KEY_MESSAGE_FLOW_CONFIG).getNewValue());
            }
            if (changeEvent.isChanged(APOLLO_KEY_MESSAGE_DELAY_CONFIG)) {
                updateDelayConfig(changeEvent.getChange(APOLLO_KEY_MESSAGE_DELAY_CONFIG).getNewValue());
            }
        });
    }

    private void updateFlowConfig(String config) {
        Optional.ofNullable(JSON.parseObject(config, new TypeReference<Map<String, FlowConfig>>() {
        })).ifPresent(map -> flowConfig = map);
    }

    private void updateDelayConfig(String config) {
        Optional.ofNullable(JSON.parseObject(config, new TypeReference<Map<String, Long>>() {
        })).ifPresent(map -> delayConfig = map);
    }

    public Map<String, FlowConfig> getFlowConfig() {
        return flowConfig;
    }

    public Map<String, Long> getDelayConfig() {
        return delayConfig;
    }
}
