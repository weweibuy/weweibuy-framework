package com.weweibuy.framework.common.feign.config;

import feign.Client;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.client.HttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author durenhao
 * @date 2020/9/9 17:43
 **/
@Configuration
@ConditionalOnClass(name = {"org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient",
        "org.springframework.cloud.netflix.ribbon.SpringClientFactory"})
public class CommonLoadBalancerConfig {

    @Bean
    @ConditionalOnClass(name = {"org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient",
            "org.springframework.cloud.netflix.ribbon.SpringClientFactory"})
    public Client loadBalancerFeignClient(CachingSpringLoadBalancerFactory cachingFactory,
                                          SpringClientFactory clientFactory, HttpClient httpClient) {
        ApacheHttpClient apacheHttpClient = new ApacheHttpClient(httpClient);
        return new LoadBalancerFeignClient(apacheHttpClient, cachingFactory, clientFactory);
    }


}
