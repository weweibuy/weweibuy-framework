package com.weweibuy.framework.rocketmq.annotation;

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

    String NAME_SERVER_PLACEHOLDER = "${rocket-mq.name-server:}";
    String ACCESS_KEY_PLACEHOLDER = "${rocket-mq.consumer.access-key:}";
    String SECRET_KEY_PLACEHOLDER = "${rocket-mq.consumer.secret-key:}";
    String TRACE_TOPIC_PLACEHOLDER = "${rocket-mq.consumer.customized-trace-topic:}";
    String ACCESS_CHANNEL_PLACEHOLDER = "${rocket-mq.access-channel:}";

    String name() default "";

    String topic();

    String group();

    boolean orderly() default false;

    long timeout() default 30000L;

    int maxRetry() default 16;

    int threadMin() default 10;

    int threadMax() default 10;

    int consumeMessageBatchMaxSize() default 1;

    String accessKey() default ACCESS_KEY_PLACEHOLDER;

    String secretKey() default SECRET_KEY_PLACEHOLDER;

    String accessChannel() default ACCESS_CHANNEL_PLACEHOLDER;


}
