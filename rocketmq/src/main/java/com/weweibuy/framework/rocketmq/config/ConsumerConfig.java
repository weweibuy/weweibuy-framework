package com.weweibuy.framework.rocketmq.config;

import com.weweibuy.framework.rocketmq.core.consumer.DefaultRocketListenerContainerFactory;
import com.weweibuy.framework.rocketmq.core.consumer.RocketBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author durenhao
 * @date 2020/1/4 20:49
 **/
@Configuration
public class ConsumerConfig {


    @Bean
    public RocketBeanPostProcessor rocketBeanPostProcessor(RocketMqProperties rocketMqProperties) {
        return new RocketBeanPostProcessor(new DefaultRocketListenerContainerFactory(), rocketMqProperties);
    }


}
