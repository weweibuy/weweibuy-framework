package com.weweibuy.framework.idempotent.core.annotation;

import com.weweibuy.framework.idempotent.core.support.IdempotentExpressionRootObject;
import com.weweibuy.framework.idempotent.core.support.IdempotentManager;

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
     * {@link IdempotentExpressionRootObject}
     *
     * @return
     */
    String key();

    /**
     * 幂等数据分片,适用于JDBC情况下, 可使用 SPEL 表达式
     * {@link IdempotentExpressionRootObject}
     *
     * @return
     */
    String sharding() default "";

    /**
     * 加锁最长时间, 适用于Redis 锁定时间
     *
     * @return
     */
    long maxLockMilli() default 10000L;

    /**
     * key 生成器, 指定springBeanName
     *
     * @return
     */
    String generator() default "";

    /**
     * 指定幂等管理器BeanName, 多个管理器情况下使用 @Primary 的管理器
     * {@link IdempotentManager}
     *
     * @return
     */
    String idempotentManager() default "";

}
