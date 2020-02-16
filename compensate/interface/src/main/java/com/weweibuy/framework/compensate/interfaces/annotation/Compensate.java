package com.weweibuy.framework.compensate.interfaces.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author durenhao
 * @date 2020/2/13 20:22
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Compensate {

    /**
     * 标识哪一种类型的补偿
     *
     * @return
     */
    String key();

    /**
     * 业务ID
     *
     * @return
     */
    String bizId() default "";

    /**
     * 补偿包含的异常
     *
     * @return
     */
    Class<? extends Throwable>[] include() default {Exception.class};

    /**
     * 补偿不包含的异常
     *
     * @return
     */
    Class<? extends Throwable>[] exclude() default {};

    /**
     * 标记恢复方法
     *
     * @return
     */
    Recover recover() default @Recover();

}
