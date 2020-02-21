package com.weweibuy.framework.rocketmq.config;

import com.weweibuy.framework.rocketmq.core.MessageConverter;
import com.weweibuy.framework.rocketmq.core.consumer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/1/4 20:49
 **/
@Configuration
public class ConsumerConfig {

    @Autowired(required = false)
    private RocketListenerErrorHandler errorHandler;

    @Autowired(required = false)
    private List<RocketConfigurer> configurer;


    @Bean
    public RocketBeanPostProcessor rocketBeanPostProcessor(RocketMqProperties rocketMqProperties, MessageConverter messageConverter) {

        HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();
        composite.addResolver(new PayloadMethodArgumentResolver(messageConverter));
        composite.addResolver(new HeaderMethodArgumentResolver());

        if (!CollectionUtils.isEmpty(configurer)) {
            configurer.forEach(c -> c.addHandlerMethodArgumentResolver(composite));
        }

        return new RocketBeanPostProcessor(new DefaultRocketListenerContainerFactory(), messageHandlerMethodFactory(),
                rocketMqProperties, errorHandler, composite);
    }

    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory() {
        return new DefaultMessageHandlerMethodFactory();
    }


}
