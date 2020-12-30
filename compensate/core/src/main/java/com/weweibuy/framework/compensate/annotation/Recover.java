package com.weweibuy.framework.compensate.annotation;

import com.weweibuy.framework.compensate.config.CompensateConfigurer;
import com.weweibuy.framework.compensate.core.CompensateHandlerService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author durenhao
 * @date 2020/2/13 20:29
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Recover {

    /**
     * spring beanName
     *
     * @return
     */
    String beanName() default "";

    /**
     * 方法名
     *
     * @return
     */
    String method() default "";

    /**
     * 异步执行,Recover方法; 仅在 {@link CompensateHandlerService#executorService}
     * 不为空的情况下生效
     * {@link CompensateConfigurer#getCompensateExecutorService()}
     *
     * @return
     */
    boolean async() default false;

}
