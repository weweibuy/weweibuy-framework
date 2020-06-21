package com.weweibuy.framework.rocketmq.annotation;

import com.weweibuy.framework.rocketmq.config.RocketMqProperties;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记类为rocketMQ的消费类, 配合 {@link RocketConsumerHandler} 一起使用消费MQ消息
 *
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

    /**
     * 是否顺序消费
     *
     * @return
     */
    boolean orderly() default false;

    /**
     * 最大的消费超时时间 单位分钟, 如果消费超时，RocketMQ会等同于消费失败来处理
     * https://zhuanlan.zhihu.com/p/25265380
     *
     * @return
     */
    long timeout() default 15L;

    /**
     * 最大重试消费次数
     *
     * @return
     */
    int maxRetry() default 16;

    /**
     * 最小消费线程数
     *
     * @return
     */
    int threadMin() default 10;

    /**
     * 最大消费线程数
     *
     * @return
     */
    int threadMax() default 10;

    /**
     * 每次消费消息个数
     *
     * @return
     */
    int consumeMessageBatchMaxSize() default 1;

    /**
     * 消费模式
     *
     * @return
     */
    MessageModel messageModel() default MessageModel.CLUSTERING;
}
