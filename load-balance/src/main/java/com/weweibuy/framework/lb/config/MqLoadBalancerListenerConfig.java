package com.weweibuy.framework.lb.config;

import com.weweibuy.framework.lb.mq.RocketServerChangeListener;
import com.weweibuy.framework.lb.support.LoadBalanceOperator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author durenhao
 * @date 2021/3/14 21:05
 **/
@AutoConfiguration
@ConditionalOnBean(type = "com.weweibuy.framework.rocketmq.core.consumer.RocketBeanPostProcessor")
@ConditionalOnProperty(name = "common.lb.server-change-listener.enable", havingValue = "true")
public class MqLoadBalancerListenerConfig {

    @Bean
    public RocketServerChangeListener rocketServerChangeListener(LoadBalanceOperator loadBalanceOperator) {
        return new RocketServerChangeListener(loadBalanceOperator);
    }

}
