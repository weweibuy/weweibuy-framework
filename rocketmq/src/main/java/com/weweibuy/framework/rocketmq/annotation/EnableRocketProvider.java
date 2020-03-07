package com.weweibuy.framework.rocketmq.annotation;

import com.weweibuy.framework.rocketmq.config.MessageConverterConfig;
import com.weweibuy.framework.rocketmq.config.ProviderConfig;
import com.weweibuy.framework.rocketmq.core.provider.RocketProviderRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author durenhao
 * @date 2019/12/28 21:58
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({RocketProviderRegister.class, ProviderConfig.class, MessageConverterConfig.class})
public @interface EnableRocketProvider {

    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

}
