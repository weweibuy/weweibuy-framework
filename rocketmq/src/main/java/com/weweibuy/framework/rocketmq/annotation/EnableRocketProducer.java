package com.weweibuy.framework.rocketmq.annotation;

import com.weweibuy.framework.rocketmq.config.RocketMqConfig;
import com.weweibuy.framework.rocketmq.config.ProducerConfig;
import com.weweibuy.framework.rocketmq.core.producer.RocketProducerRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启rocketMq生产者
 *
 * @author durenhao
 * @date 2019/12/28 21:58
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({RocketProducerRegister.class, ProducerConfig.class, RocketMqConfig.class})
public @interface EnableRocketProducer {

    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

}
