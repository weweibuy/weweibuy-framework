package com.weweibuy.framework.common.feign.config;

import com.weweibuy.framework.common.feign.support.CustomHttpClientLogInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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


}
