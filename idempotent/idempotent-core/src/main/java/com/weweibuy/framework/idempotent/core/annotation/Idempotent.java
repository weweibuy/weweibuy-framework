package com.weweibuy.framework.idempotent.core.annotation;

import com.weweibuy.framework.idempotent.core.aspect.IdempotentAspect;
import com.weweibuy.framework.idempotent.core.exception.IdempotentNoLockException;
import com.weweibuy.framework.idempotent.core.support.IdempotentExpressionRootObject;
import com.weweibuy.framework.idempotent.core.support.IdempotentManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 幂等注解,并该注解标记的方法将被Aop切入
 * {@link IdempotentAspect}
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
     * 加锁最长时间, 用于Redis做幂等
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
     * 指定幂等管理器BeanName, 多个管理器情况下使用 @Primary 的管理器, 默认的是 JDBC方式
     * <p>
     * 基于数据库的幂等必须要有事务的保证,尽量避免大事务,因为可能导致相同的幂等key的调用者一直处于阻塞状态;
     * <p>
     * 基于Redis的幂等在无法获取到锁后, 会抛出 {@link IdempotentNoLockException}, 需要上游处理
     * 并且在业务方法开始时,需要自行手动查询业务是否已经执行过,如果已经执行过需要手动,需要告知调用方
     * <p>
     * {@link IdempotentManager}
     *
     * @return
     */
    String idempotentManager() default "";

}
