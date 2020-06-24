package com.weweibuy.framework.rocketmq.config;

import com.weweibuy.framework.rocketmq.core.MessageConverter;
import com.weweibuy.framework.rocketmq.core.consumer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author durenhao
 * @date 2020/1/4 20:49
 **/
@Configuration
public class ConsumerConfig {


    @Autowired(required = false)
    private List<RocketConfigurer> configurerList;

    @Autowired(required = false)
    private RocketListenerErrorHandler errorHandler;


    @Bean
    public RocketBeanPostProcessor rocketBeanPostProcessor(RocketMqProperties rocketMqProperties, MessageConverter messageConverter) {

        HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();
        composite.addResolver(new PayloadMethodArgumentResolver(messageConverter));
        composite.addResolver(new HeaderMethodArgumentResolver());

        List<ConsumerFilter> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(configurerList)) {
            configurerList.forEach(c -> {
                c.addHandlerMethodArgumentResolver(composite);
                c.addConsumerFilter(list);
            });
        }

        RocketBeanPostProcessor postProcessor = new RocketBeanPostProcessor(new DefaultRocketListenerContainerFactory(),
                rocketMessageHandlerMethodFactory(),
                rocketMqProperties, composite);


        postProcessor.setConsumerFilterList(list);
        postProcessor.setErrorHandler(errorHandler);
        return postProcessor;

    }

    @Bean
    public MessageHandlerMethodFactory rocketMessageHandlerMethodFactory() {
        return new DefaultMessageHandlerMethodFactory();
    }


}
