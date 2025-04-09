package com.weweibuy.framework.lb.config;

import com.weweibuy.framework.lb.nacos.NacosServiceChangeListener;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * @date 2025/4/9
 **/
@AutoConfiguration
@ConditionalOnBean(type = {"com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery",
        "com.alibaba.cloud.nacos.NacosServiceManager"})
public class NacosLoadBalancerListenerConfig {


    @Bean
    public NacosServiceChangeListener nacosServiceChangeListener() {
        return new NacosServiceChangeListener();
    }

}
