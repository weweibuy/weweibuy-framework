package com.weweibuy.framework.compensate.annotation;

import java.lang.annotation.*;

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
    Class<? extends Throwable>[] include() default {};

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
