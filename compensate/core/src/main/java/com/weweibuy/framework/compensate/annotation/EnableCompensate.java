package com.weweibuy.framework.compensate.annotation;

import com.weweibuy.framework.compensate.config.AdvisorConfig;
import com.weweibuy.framework.compensate.config.CompensateConfigurationSupport;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author durenhao
 * @date 2020/2/13 21:23
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({CompensateConfigurationSupport.class, AdvisorConfig.class})
public @interface EnableCompensate {

    int order() default Ordered.LOWEST_PRECEDENCE;

}
