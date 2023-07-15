package com.weweibuy.framework.common.feign.config;

import com.weweibuy.framework.common.feign.support.DelegateFeignClient;
import feign.Client;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/9/9 17:43
 **/
@AutoConfiguration
@ConditionalOnClass(name = {"org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory"})
public class CommonLoadBalancerConfig {

    @Autowired(required = false)
    private List<DelegateFeignClient> delegateFeignClientList;

    @Bean
    @ConditionalOnMissingBean(value = {LoadBalancerClient.class, HttpClient.class,
            LoadBalancerProperties.class, LoadBalancerClientFactory.class})
    public Client feignClient(LoadBalancerClient loadBalancerClient,
                              HttpClient httpClient,
                              LoadBalancerClientFactory loadBalancerClientFactory) {
        Client client = new ApacheHttpClient(httpClient);
        client = DelegateFeignClient.delegateChain(delegateFeignClientList, client);
        return new FeignBlockingLoadBalancerClient(client, loadBalancerClient, loadBalancerClientFactory);
    }


}
