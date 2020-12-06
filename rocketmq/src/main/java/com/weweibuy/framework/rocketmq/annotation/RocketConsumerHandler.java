package com.weweibuy.framework.rocketmq.annotation;

import com.weweibuy.framework.rocketmq.core.consumer.RocketBeanPostProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记处理 方法为 Rocket 消费方法,  配合 {@link RocketListener} 一起使用
 * {@link RocketBeanPostProcessor }
 *
 * @author durenhao
 * @date 2020/1/4 18:18
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RocketConsumerHandler {

    /**
     * 消费的Tag, 支持EL表达式
     *
     * @return
     */
    String tags() default "*";


}
