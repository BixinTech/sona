package cn.bixin.sona.gateway.interceptor;


import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.common.AccessMessage;

/**
 * @author qinwei
 * <p>
 * 业务 handler 拦截器 ,在实现类上添加注解 {@link Interceptor}, 指定需要拦截的handler (默认拦截所有)
 * 并且支持下面几种方式排序
 * @see org.springframework.core.Ordered
 * @see org.springframework.core.annotation.Order
 * @see javax.annotation.Priority
 */
public interface HandlerInterceptor {

    default boolean preHandle(NettyChannel channel, AccessMessage message) throws Exception {
        return true;
    }

    default void postHandle(NettyChannel channel, AccessMessage message) throws Exception {

    }

    default void afterHandle(NettyChannel channel, AccessMessage message, Exception ex) throws Exception {

    }

}
