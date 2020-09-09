package com.weweibuy.framework.common.feign.config;

import feign.Client;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author durenhao
 * @date 2020/9/9 17:43
 **/
@Configuration
@ConditionalOnClass(name = {"org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient",
        "org.springframework.cloud.netflix.ribbon.SpringClientFactory"})
public class CommonLoadBalancerConfig {

    @Bean
    @Primary
    @ConditionalOnClass(name = {"org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient",
            "org.springframework.cloud.netflix.ribbon.SpringClientFactory"})
    public Client loadBalancerFeignClient(CachingSpringLoadBalancerFactory cachingFactory,
                                          SpringClientFactory clientFactory, Client client) {
        return new LoadBalancerFeignClient(client, cachingFactory, clientFactory);
    }


}
