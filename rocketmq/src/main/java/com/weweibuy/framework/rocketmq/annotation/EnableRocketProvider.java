package com.weweibuy.framework.rocketmq.annotation;

import com.weweibuy.framework.rocketmq.core.RocketProviderRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author durenhao
 * @date 2019/12/28 21:58
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(RocketProviderRegister.class)
public @interface EnableRocketProvider {

    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

}
