package com.weweibuy.framework.rocketmq.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author durenhao
 * @date 2019/12/29 17:20
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RocketProviderHandler {

    /**
     * 消息TAG
     *
     * @return
     */
    String tag() default "";

    /**
     * 消息key
     *
     * @return
     */
    String key() default "";

    /**
     * 发送超时
     *
     * @return
     */
    int timeout() default 3000;

    /**
     * 批量,发送, 批量发送时对应需要转为message的参数必须为
     * {@link java.util.Collection}  类型
     *
     * @return
     */
    boolean batch() default false;

    /**
     * 是否顺序发送, 使用顺序发送必须指定 RocketProviderHandler.key(), 对key去hash 选择队列
     * {@link org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash}
     *
     * @return
     */
    boolean orderly() default false;

    /**
     * 是否 oneWay 发送;
     * oneWay 如果为 true, 被标记的方法如果有返回值, 将返回 null
     *
     * @return
     */
    boolean oneWay() default false;


}
