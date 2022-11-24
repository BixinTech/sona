package cn.bixin.sona.gateway.interceptor;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author qinwei
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Interceptor {

    String[] name() default {};
}
