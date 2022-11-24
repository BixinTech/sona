package cn.bixin.sona.gateway.interceptor;

import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.common.AccessMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qinwei
 */
@Slf4j
@Component
public class HandlerInterceptorChain implements ApplicationContextAware {

    private static List<HandlerInterceptor> allInterceptors;

    private final List<HandlerInterceptor> interceptors;

    private int interceptorIndex = -1;

    private HandlerInterceptorChain(List<HandlerInterceptor> interceptors) {
        this.interceptors = interceptors;
        AnnotationAwareOrderComparator.sort(this.interceptors);
    }

    public static void init(Map<String, HandlerInterceptor> handlerInterceptors) {
        allInterceptors = new ArrayList<>(handlerInterceptors.values());
    }

    public static HandlerInterceptorChain getHandlerInterceptorChain(String name) {
        List<HandlerInterceptor> list = allInterceptors.stream().filter(interceptor -> match(name, interceptor)).collect(Collectors.toList());
        return new HandlerInterceptorChain(list);
    }

    private static boolean match(String name, HandlerInterceptor interceptor) {
        Interceptor annotation = AnnotationUtils.findAnnotation(interceptor.getClass(), Interceptor.class);
        if (annotation == null) {
            return false;
        }
        return ObjectUtils.isEmpty(annotation.name()) || Arrays.asList(annotation.name()).contains(name);
    }

    public boolean applyPreHandle(NettyChannel channel, AccessMessage message) throws Exception {
        if (!CollectionUtils.isEmpty(interceptors)) {
            for (int i = 0; i < interceptors.size(); i++) {
                HandlerInterceptor interceptor = interceptors.get(i);
                if (!interceptor.preHandle(channel, message)) {
                    applyAfterHandle(channel, message, null);
                    return false;
                }
                this.interceptorIndex = i;
            }
        }
        return true;
    }

    public void applyPostHandle(NettyChannel channel, AccessMessage message) throws Exception {
        if (!ObjectUtils.isEmpty(interceptors)) {
            for (int i = interceptors.size() - 1; i >= 0; i--) {
                HandlerInterceptor interceptor = interceptors.get(i);
                interceptor.postHandle(channel, message);
            }
        }
    }

    public void applyAfterHandle(NettyChannel channel, AccessMessage message, Exception ex) {
        if (!CollectionUtils.isEmpty(interceptors)) {
            for (int i = interceptorIndex; i > 0; i--) {
                HandlerInterceptor interceptor = interceptors.get(i);
                try {
                    interceptor.afterHandle(channel, message, ex);
                } catch (Throwable t) {
                    log.error("HandlerInterceptor.afterHandle threw exception", t);
                }
            }
            this.interceptorIndex = -1;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        init(applicationContext.getBeansOfType(HandlerInterceptor.class));
    }
}
