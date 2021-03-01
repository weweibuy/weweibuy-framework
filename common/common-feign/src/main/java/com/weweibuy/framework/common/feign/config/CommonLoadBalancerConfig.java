package com.weweibuy.framework.common.feign.config;

import feign.Client;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.client.HttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author durenhao
 * @date 2020/9/9 17:43
 **/
@Configuration
@ConditionalOnClass(name = {"org.springframework.cloud.client.loadbalancer.LoadBalancerClient",
        "org.springframework.cloud.client.loadbalancer.LoadBalancerProperties",
        "org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory"})
public class CommonLoadBalancerConfig {


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = {"org.springframework.cloud.client.loadbalancer.LoadBalancerClient",
            "org.springframework.cloud.client.loadbalancer.LoadBalancerProperties",
            "org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory"})
    public Client feignClient(LoadBalancerClient loadBalancerClient, HttpClient httpClient,
                              LoadBalancerProperties properties, LoadBalancerClientFactory loadBalancerClientFactory) {
        ApacheHttpClient delegate = new ApacheHttpClient(httpClient);
        return new FeignBlockingLoadBalancerClient(delegate, loadBalancerClient, properties, loadBalancerClientFactory);
    }



}
