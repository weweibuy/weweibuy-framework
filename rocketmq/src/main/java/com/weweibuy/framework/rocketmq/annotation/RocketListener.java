package com.weweibuy.framework.rocketmq.annotation;

import com.weweibuy.framework.rocketmq.config.RocketMqProperties;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author durenhao
 * @date 2020/1/4 17:13
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RocketListener {

    /**
     * name  默认为springBean 的名字
     * {@link RocketMqProperties#consumer}  中对应name的 consumer 会覆盖代码中的配置
     *
     * @return
     */
    String name() default "";

    /**
     * 支持  ${} 占位的形式
     *
     * @return
     */
    String topic();

    /**
     * 支持  ${} 占站位的形式
     *
     * @return
     */
    String group();

    boolean orderly() default false;

    long timeout() default 30000L;

    int maxRetry() default 16;

    int threadMin() default 10;

    int threadMax() default 10;

    int consumeMessageBatchMaxSize() default 1;

    /**
     * 消费模式
     *
     * @return
     */
    MessageModel messageModel() default MessageModel.CLUSTERING;
}
