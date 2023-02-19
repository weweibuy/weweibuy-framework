package com.weweibuy.framework.common.mvc.config;

import com.weweibuy.framework.common.core.support.ReadableBodyRequestHandler;
import com.weweibuy.framework.common.core.support.ReadableBodyResponseHandler;
import com.weweibuy.framework.common.mvc.advice.*;
import com.weweibuy.framework.common.mvc.endpoint.CustomHealthController;
import com.weweibuy.framework.common.mvc.filter.ReadableBodyFilter;
import com.weweibuy.framework.common.mvc.support.DefaultFeignExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/3/2 17:53
 **/
@AutoConfiguration
public class CommonMvcConfig {

    @Autowired(required = false)
    private List<FeignMethodKeyMappingConverter> feignMethodKeyMappingConverterList;

    @Autowired(required = false)
    private List<ReadableBodyRequestHandler> readableBodyRequestHandler;

    @Autowired(required = false)
    private List<ReadableBodyResponseHandler> readableBodyResponseHandler;

    @Bean
    public CommonExceptionAdvice commonExceptionAdvice() {
        return new CommonExceptionAdvice();
    }

    @Bean
    @ConditionalOnClass(name = {"feign.Feign", "com.weweibuy.framework.common.core.exception.MethodKeyFeignException"})
    public FeignExceptionAdvice feignExceptionAdvice() {
        return new FeignExceptionAdvice();
    }

    @Bean
    @ConditionalOnClass(name = {"org.springframework.transaction.TransactionManager"})
    public BdExceptionAdvice bdExceptionAdvice() {
        return new BdExceptionAdvice();
    }

    @Bean
    public CustomHealthController customHealthController() {
        return new CustomHealthController();
    }

    @Bean
    public ReadableBodyFilter readableBodyFilter() {
        return new ReadableBodyFilter(readableBodyRequestHandler, readableBodyResponseHandler);
    }

    @Bean
    @Primary
    public CommonErrorAttributes commonErrorAttributes() {
        return new CommonErrorAttributes();
    }

    @Bean
    @ConditionalOnClass(name = {"feign.Feign", "com.weweibuy.framework.common.core.exception.MethodKeyFeignException"})
    public DefaultFeignExceptionHandler defaultFeignExceptionHandler() {
        return new DefaultFeignExceptionHandler(feignMethodKeyMappingConverterList);
    }


}
