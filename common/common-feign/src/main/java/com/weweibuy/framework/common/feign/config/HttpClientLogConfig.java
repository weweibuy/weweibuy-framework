package com.weweibuy.framework.common.feign.config;

import com.weweibuy.framework.common.feign.mock.MockFeignLogFilter;
import com.weweibuy.framework.common.feign.support.CustomHttpClientLogInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author durenhao
 * @date 2022/10/15 11:45
 **/
@AutoConfiguration
@ConditionalOnProperty(prefix = "common.feign.log", name = "position", havingValue = "httpclient", matchIfMissing = true)
public class HttpClientLogConfig {


    @Bean
    public CustomHttpClientLogInterceptor customHttpClientLogInterceptor() {
        return new CustomHttpClientLogInterceptor();
    }

    @Bean
    @Profile(value = {"mock"})
    public MockFeignLogFilter mockFeignLogFilter() {
        return new MockFeignLogFilter();
    }

}
