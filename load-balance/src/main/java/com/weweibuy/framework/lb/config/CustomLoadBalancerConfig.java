package com.weweibuy.framework.lb.config;

import com.weweibuy.framework.lb.endpoint.LoadBalanceEndpoint;
import com.weweibuy.framework.lb.support.LoadBalanceOperator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 负载均衡相关配置
 *
 * @author durenhao
 * @date 2020/9/25 11:13
 **/
@AutoConfiguration
@EnableConfigurationProperties({LoadBalanceProperties.class})
public class CustomLoadBalancerConfig {

    /**
     * 负载均衡,增加的controller端点
     *
     * @return
     */
    @Bean
    public LoadBalanceEndpoint customLoadBalanceEndpoint() {
        return new LoadBalanceEndpoint(loadBalanceOperator());
    }


    /**
     * 负载均衡,缓存服务相关操作类
     *
     * @return
     */
    @Bean
    public LoadBalanceOperator loadBalanceOperator() {
        return new LoadBalanceOperator();
    }


}
