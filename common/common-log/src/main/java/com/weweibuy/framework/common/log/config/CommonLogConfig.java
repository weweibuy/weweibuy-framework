package com.weweibuy.framework.common.log.config;

import com.weweibuy.framework.common.log.mvc.RequestLogContextFilter;
import com.weweibuy.framework.common.log.mvc.RequestResponseBodyLogAdvice;
import com.weweibuy.framework.common.log.mvc.TraceCodeFilter;
import com.weweibuy.framework.common.log.mvc.UnRequestBodyJsonLogInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author durenhao
 * @date 2020/3/1 10:39
 **/
@Configuration
@ConditionalOnProperty(prefix = "common.log", name = "enable", havingValue = "true", matchIfMissing = true)
public class CommonLogConfig implements WebMvcConfigurer{


    @Bean
    public RequestLogContextFilter requestLogContextFilter() {
        return new RequestLogContextFilter();
    }

    @Bean
    @ConditionalOnProperty(prefix = "common.log.trace", name = "enable", havingValue = "true")
    public TraceCodeFilter traceCodeFilter() {
        return new TraceCodeFilter();
    }

    @Bean
    public RequestResponseBodyLogAdvice requestResponseBodyLogAdvice() {
        return new RequestResponseBodyLogAdvice();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UnRequestBodyJsonLogInterceptor());
    }
}
