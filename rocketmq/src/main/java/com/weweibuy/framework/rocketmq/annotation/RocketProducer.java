package com.weweibuy.framework.rocketmq.annotation;

import java.lang.annotation.*;

/**
 * 标记接口,将被spring 代理, 可以作为一个rocketMq的生产者,
 * 配合 {@link RocketProducerHandler} 一起使用
 *
 * @author durenhao
 * @date 2019/12/28 22:48
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RocketProducer {

    /**
     * MQ topic
     *
     * @return
     */
    String topic();


}
