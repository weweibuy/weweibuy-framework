package com.weweibuy.framework.rocketmq.config;

import com.weweibuy.framework.rocketmq.core.MessageConverter;
import com.weweibuy.framework.rocketmq.core.consumer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author durenhao
 * @date 2020/1/4 20:49
 **/
@Configuration
public class ConsumerConfig {

    @Autowired(required = false)
    private RocketListenerErrorHandler errorHandler;

    @Autowired(required = false)
    private HandlerMethodArgumentResolverComposite argumentResolverComposite;

    @Bean
    public RocketBeanPostProcessor rocketBeanPostProcessor(RocketMqProperties rocketMqProperties, MessageConverter messageConverter) {

        return new RocketBeanPostProcessor(new DefaultRocketListenerContainerFactory(), messageHandlerMethodFactory(),
                rocketMqProperties, messageConverter, errorHandler, argumentResolverComposite);
    }

    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory() {
        return new DefaultMessageHandlerMethodFactory();
    }


}
