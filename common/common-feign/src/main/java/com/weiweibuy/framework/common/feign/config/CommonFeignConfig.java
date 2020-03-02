package com.weiweibuy.framework.common.feign.config;

import com.weiweibuy.framework.common.feign.log.FeignLogRequestInterceptor;
import com.weiweibuy.framework.common.feign.log.LogEncoderAndDecoder;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
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

    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder(Retryer retryer) {
        return Feign.builder()
//                .options(new Request.Options(100, 200))
                .retryer(retryer)
                .logLevel(Logger.Level.FULL);
    }

    @Bean
    public Retryer feignRetryer() {
        return Retryer.NEVER_RETRY;
    }

    @Bean
    public RequestInterceptor feignLogRequestInterceptor() {
        return new FeignLogRequestInterceptor();
    }

    @Bean
    public LogEncoderAndDecoder logEncoderAndDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new LogEncoderAndDecoder(messageConverters);
    }


}