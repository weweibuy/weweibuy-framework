package com.weweibuy.framework.rocketmq.config;

import com.weweibuy.framework.rocketmq.core.MessageConverter;
import com.weweibuy.framework.rocketmq.core.consumer.DefaultRocketListenerContainerFactory;
import com.weweibuy.framework.rocketmq.core.consumer.RocketBeanPostProcessor;
import com.weweibuy.framework.rocketmq.core.consumer.RocketListenerErrorHandler;
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

    @Bean
    public RocketBeanPostProcessor rocketBeanPostProcessor(RocketMqProperties rocketMqProperties, MessageConverter messageConverter) {
        return new RocketBeanPostProcessor(new DefaultRocketListenerContainerFactory(), rocketMqProperties, messageConverter, errorHandler);
    }


}
