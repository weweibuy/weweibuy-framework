package com.weweibuy.framework.rocketmq.annotation;

import java.lang.annotation.*;

/**
 * @author durenhao
 * @date 2019/12/28 22:48
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RocketProvider {

    /**
     * MQ topic
     *
     * @return
     */
    String topic();


}
