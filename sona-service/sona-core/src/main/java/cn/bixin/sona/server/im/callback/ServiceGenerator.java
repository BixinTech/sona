package cn.bixin.sona.server.im.callback;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author qinwei
 */
@Component
public class ServiceGenerator {

    private static final ConcurrentMap<String, ReferenceConfig<?>> SERVICE_HOLDER_MAP = new ConcurrentHashMap<>();

    @Value("${spi.msg.productCode:}")
    private String productCodes;

    @Resource
    private ApplicationConfig applicationConfig;

    @Resource
    private RegistryConfig registryConfig;

    public <T> T getService(Class<T> interfaceClass, String productCode) {
        String groupName = "default";
        if (contain(productCode)) {
            groupName = "sona_" + productCode;
        }
        return getService(interfaceClass, groupName, false);
    }

    public <T> T getService(Class<T> interfaceClass, String group, boolean async) {
        ReferenceConfig<?> referenceConfig = SERVICE_HOLDER_MAP.computeIfAbsent(generateKey(interfaceClass, group), s -> generateReferenceConfig(interfaceClass, group, async));
        return interfaceClass.cast(referenceConfig.get());
    }

    private String generateKey(Class<?> interfaceClass, String group) {
        StringBuilder key = new StringBuilder();
        if (StringUtils.hasText(group)) {
            key.append(group).append("/");
        }
        key.append(interfaceClass.getName());
        return key.toString();
    }

    private <T> ReferenceConfig<T> generateReferenceConfig(Class<T> interfaceClass, String group, boolean async) {
        ReferenceConfig<T> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setInterface(interfaceClass);
        referenceConfig.setGroup(group);
        referenceConfig.setTimeout(1000);
        referenceConfig.setRetries(0);
        referenceConfig.setCheck(false);
        referenceConfig.setAsync(async);
        return referenceConfig;
    }

    private boolean contain(String productCode) {
        return Arrays.stream(productCodes.split(",")).anyMatch(code -> code.equalsIgnoreCase(productCode));
    }

}
