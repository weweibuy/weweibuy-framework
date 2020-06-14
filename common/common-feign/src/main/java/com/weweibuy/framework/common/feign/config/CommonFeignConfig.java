package com.weweibuy.framework.common.feign.config;

import com.weweibuy.framework.common.feign.log.FeignLogger;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author durenhao
 * @date 2020/3/2 19:39
 **/
@Configuration
@Slf4j
public class CommonFeignConfig {

    /**
     * feign 超时覆盖机制,  配置 feign.client.config.xxx.readTimeout 最优先
     * 其次: feign.client.config.default
     * 其次:  org.springframework.cloud.openfeign.FeignClient#configuration() 中
     * 最次是builder 中配置
     *
     * @param retryer
     * @return
     */
    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder(Retryer retryer) {
       return Feign.builder()
                .retryer(retryer)
                .logLevel(Logger.Level.BASIC);
    }


    @Bean
    public FeignLogger feignLogger(){
        return new FeignLogger();
    }

    @Bean
    public Retryer feignRetryer() {
        return Retryer.NEVER_RETRY;
    }


}
