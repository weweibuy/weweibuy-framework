package com.weweibuy.framework.rocketmq.config;

import com.weweibuy.framework.rocketmq.core.consumer.ConsumerFilter;
import com.weweibuy.framework.rocketmq.core.consumer.HandlerMethodArgumentResolverComposite;
import com.weweibuy.framework.rocketmq.core.provider.AnnotatedParameterProcessorComposite;
import com.weweibuy.framework.rocketmq.core.provider.MessageSendFilter;

import java.util.List;

/**
 * rocket 生产者配置
 *
 * @author durenhao
 * @date 2020/2/20 21:44
 **/
public interface RocketConfigurer {


    default void addAnnotatedParameterProcessor(AnnotatedParameterProcessorComposite composite) {
    }

    default void addHandlerMethodArgumentResolver(HandlerMethodArgumentResolverComposite composite) {
    }

    /**
     * 增加 消费过滤器
     *
     * @param consumerFilter
     */
    default void addConsumerFilter(List<ConsumerFilter> consumerFilter) {
    }

    /**
     * 增加发送消息时过滤器
     *
     * @param messageSendFilter
     */
    default void addMessageSendFilter(List<MessageSendFilter> messageSendFilter) {
    }


}
