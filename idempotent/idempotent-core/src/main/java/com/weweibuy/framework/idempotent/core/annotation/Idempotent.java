package com.weweibuy.framework.idempotent.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 幂等注解
 *
 * @author durenhao
 * @date 2020/3/31 14:41
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {

    /**
     * 幂等 key, 可以使用  SPEL 表达式
     *
     * @return
     */
    String key();

    /**
     * 幂等数据分片, 可使用 SPEL 表达式
     *
     * @return
     */
    String sharding() default "";

    /**
     * key 生成器
     *
     * @return
     */
    String generator() default "";


}
