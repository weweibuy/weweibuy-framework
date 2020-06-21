package com.weweibuy.framework.rocketmq.annotation;

import com.weweibuy.framework.rocketmq.core.consumer.PayloadMethodArgumentResolver;
import com.weweibuy.framework.rocketmq.support.PayloadParameterProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记方法参数为 rocketMq的消息体
 * <p>
 * 生产时被: {@link PayloadParameterProcessor} 处理
 * 消费时被 {@link PayloadMethodArgumentResolver} 解析
 *
 * @author durenhao
 * @date 2019/12/31 16:11
 **/
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Payload {
}
