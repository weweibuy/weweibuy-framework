package com.weweibuy.framework.rocketmq.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记处理 方法为 Rocket 消费方法,  配合 {@link RocketListener} 一起使用
 *
 * @author durenhao
 * @date 2020/1/4 18:18
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RocketConsumerHandler {

    /**
     * 消费的Tag
     *
     * @return
     */
    String tags() default "*";

    /**
     * 批量处理模式
     *
     * @return
     */
    BatchHandlerModel batchHandlerModel() default BatchHandlerModel.FOREACH;

}
