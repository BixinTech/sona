package cn.bixin.sona.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author qinwei
 */
@Component
public class SpringApplicationContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private static String[] activeProfiles;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContext.applicationContext = applicationContext;
        activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        return applicationContext.getBeansOfType(type);
    }

    public static String getActiveProfile() {
        if (activeProfiles == null || activeProfiles.length == 0) {
            return null;
        }
        return activeProfiles[0];
    }

}
