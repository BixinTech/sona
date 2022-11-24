package cn.bixin.sona.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qinwei
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommonExecutor {

    /**
     * 接口描述
     */
    String desc() default "";

    /**
     * 是否打印参数日志
     */
    boolean printParam() default false;

    /**
     * 是否打印返回日志
     */
    boolean printResponse() default false;
}
