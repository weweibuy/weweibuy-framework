package com.weweibuy.framework.common.mvc.config;

import com.weweibuy.framework.common.core.support.SystemIdGetter;
import com.weweibuy.framework.common.mvc.advice.CommonErrorAttributes;
import com.weweibuy.framework.common.mvc.advice.CommonExceptionAdvice;
import com.weweibuy.framework.common.mvc.advice.FeignExceptionAdvice;
import com.weweibuy.framework.common.mvc.advice.FeignMethodKeyMappingConverter;
import com.weweibuy.framework.common.mvc.endpoint.CustomHealthController;
import com.weweibuy.framework.common.mvc.support.DefaultFeignExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/3/2 17:53
 **/
@Configuration
public class CommonMvcConfig {

    @Autowired(required = false)
    private SystemIdGetter systemIdGetter;

    @Autowired(required = false)
    private List<FeignMethodKeyMappingConverter> feignMethodKeyMappingConverterList;


    /**
     * 注意顺序必须在  {@link CommonMvcConfig#commonExceptionAdvice() 之前}
     *
     * @return
     */
    @Bean
    @ConditionalOnClass(name = {"feign.Feign", "com.weweibuy.framework.common.core.exception.MethodKeyFeignException"})
    public FeignExceptionAdvice feignExceptionAdvice() {
        return new FeignExceptionAdvice();
    }

    @Bean
    public CommonExceptionAdvice commonExceptionAdvice() {
        return new CommonExceptionAdvice();
    }

    @Bean
    public CustomHealthController customHealthController(){
        return new CustomHealthController();
    }


    @Bean
    @Primary
    public CommonErrorAttributes commonErrorAttributes() {
        return new CommonErrorAttributes(systemIdGetter);
    }

    @Bean
    @ConditionalOnClass(name = {"feign.Feign", "com.weweibuy.framework.common.core.exception.MethodKeyFeignException"})
    public DefaultFeignExceptionHandler defaultFeignExceptionHandler() {
        return new DefaultFeignExceptionHandler(feignMethodKeyMappingConverterList);
    }

    @Bean
    public HealthLogDisableConfigurer healthLogDisableConfigurer() {
        return new HealthLogDisableConfigurer();
    }

}
