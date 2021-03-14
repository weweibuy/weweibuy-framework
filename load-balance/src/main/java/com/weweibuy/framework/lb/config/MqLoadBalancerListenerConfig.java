package com.weweibuy.framework.lb.config;

import com.weweibuy.framework.lb.mq.RocketServerChangeListener;
import com.weweibuy.framework.lb.support.LoadBalanceOperator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author durenhao
 * @date 2021/3/14 21:05
 **/
@Configuration
@ConditionalOnBean(type = "com.weweibuy.framework.rocketmq.core.consumer.RocketBeanPostProcessor")
public class MqLoadBalancerListenerConfig {

    @Bean
    public RocketServerChangeListener rocketServerChangeListener(LoadBalanceOperator loadBalanceOperator) {
        return new RocketServerChangeListener(loadBalanceOperator);
    }

}
