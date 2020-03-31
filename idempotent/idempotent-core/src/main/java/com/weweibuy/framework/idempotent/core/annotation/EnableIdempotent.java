package com.weweibuy.framework.idempotent.core.annotation;

import com.weweibuy.framework.idempotent.core.config.IdempotentConfig;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author durenhao
 * @date 2020/3/31 14:59
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(IdempotentConfig.class)
public @interface EnableIdempotent {

    /**
     * 切面执行顺序
     *
     * @return
     */
    int order() default Ordered.LOWEST_PRECEDENCE;
}
