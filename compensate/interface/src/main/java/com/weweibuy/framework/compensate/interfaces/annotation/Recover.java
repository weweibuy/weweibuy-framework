package com.weweibuy.framework.compensate.interfaces.annotation;

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

}
