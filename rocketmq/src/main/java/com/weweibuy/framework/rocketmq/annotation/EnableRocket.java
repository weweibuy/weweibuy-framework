package com.weweibuy.framework.rocketmq.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 同时开启 rocketMq 生产者与消费者
 *
 * @author durenhao
 * @date 2020/1/4 20:52
 **/
@EnableRocketProducer
@EnableRocketConsumer
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableRocket {


}
