package com.weweibuy.framework.rocketmq.core.consumer;

import org.springframework.core.MethodParameter;

/**
 * 参数解析器, 用于在消费消息时,解析被 {@link com.weweibuy.framework.rocketmq.annotation.RocketConsumerHandler}
 * 标记方法上的参数
 *
 * @author durenhao
 * @date 2020/1/6 23:19
 **/
public interface HandlerMethodArgumentResolver {

    /**
     * 是否支持参数
     *
     * @param parameter
     * @return
     */
    boolean supportsParameter(MethodParameter parameter);

    /**
     * 解析参数
     *
     * @param parameter
     * @param messageObject
     * @return
     */
    Object resolveArgument(MethodParameter parameter, Object messageObject);


}
