package com.weweibuy.framework.common.feign.config;

import com.weweibuy.framework.common.core.support.SystemIdGetter;
import com.weweibuy.framework.common.feign.log.TraceContextFeignInterceptor;
import com.weweibuy.framework.common.feign.mock.MockFeignDelegateClient;
import com.weweibuy.framework.common.feign.support.CustomFeignErrorDecoder;
import com.weweibuy.framework.common.feign.support.DelegateFeignClient;
import com.weweibuy.framework.common.feign.support.FeignFilterDelegateClient;
import feign.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * feign配置
 *
 * @author durenhao
 * @date 2020/3/2 19:39
 **/
@AutoConfiguration
@RequiredArgsConstructor
public class CommonFeignConfig {

    private final SystemIdGetter systemIdGetter;



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
    public Feign.Builder feignBuilder(Retryer retryer, List<RequestInterceptor> requestInterceptorList) {
        return Feign.builder()
                .retryer(retryer)
                .logLevel(Logger.Level.NONE)
                .requestInterceptors(requestInterceptorList)
                .errorDecoder(new CustomFeignErrorDecoder(systemIdGetter))
                .options(new Request.Options(1, TimeUnit.SECONDS,
                        3, TimeUnit.SECONDS, false));
    }

    @Bean
    public TraceContextFeignInterceptor traceContextFeignInterceptor() {
        return new TraceContextFeignInterceptor();
    }


    @Bean
    public Retryer feignRetryer() {
        return Retryer.NEVER_RETRY;
    }

    @Bean
    public DelegateFeignClient delegateFeignClient() {
        return new FeignFilterDelegateClient();
    }


    @Bean
    @Profile(value = {"mock"})
    public MockFeignDelegateClient mockClient() {
        return new MockFeignDelegateClient();
    }

}
