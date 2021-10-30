package com.weweibuy.framework.common.feign.config;

import com.weweibuy.framework.common.core.support.SystemIdGetter;
import com.weweibuy.framework.common.feign.log.FeignLogger;
import com.weweibuy.framework.common.feign.log.TraceContextFeignInterceptor;
import com.weweibuy.framework.common.feign.support.*;
import feign.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * feign配置
 *
 * @author durenhao
 * @date 2020/3/2 19:39
 **/
@Configuration
@RequiredArgsConstructor
public class CommonFeignConfig {

    private final SystemIdGetter systemIdGetter;

    @Autowired(required = false)
    private List<FeignLogConfigurer> feignLogConfigurerList;


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
    public FeignLogger feignLogger() {
        List<FeignLogSetting> arrayList = new ArrayList<>();
        Optional.ofNullable(feignLogConfigurerList)
                .ifPresent(l -> l.forEach(f -> f.configurer(arrayList)));
        return new FeignLogger(arrayList);
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
    public FeignFilter logFeignFilter() {
        return new LogFeignFilter();
    }

}
