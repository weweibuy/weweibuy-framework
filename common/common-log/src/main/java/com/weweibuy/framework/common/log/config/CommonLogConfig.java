package com.weweibuy.framework.common.log.config;

import com.weweibuy.framework.common.log.logger.HttpLogger;
import com.weweibuy.framework.common.log.mvc.RequestLogContextFilter;
import com.weweibuy.framework.common.log.mvc.RequestResponseBodyLogAdvice;
import com.weweibuy.framework.common.log.mvc.TraceCodeFilter;
import com.weweibuy.framework.common.log.mvc.UnRequestBodyJsonLogInterceptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2020/3/1 10:39
 **/
@Configuration
@EnableConfigurationProperties({CommonLogProperties.class})
@ConditionalOnProperty(prefix = "common.log", name = "enable", havingValue = "true", matchIfMissing = true)
public class CommonLogConfig implements WebMvcConfigurer, InitializingBean {

    @Autowired
    private CommonLogProperties commonLogProperties;

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

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<String> disablePath = commonLogProperties.getHttp().getDisablePath();
        // 对规则匹配的进行分组
        // true为匹配型 false为精确型
        Map<Boolean, Set<String>> matchPathMap = disablePath.stream()
                .collect(Collectors.groupingBy(s -> s.indexOf("*") != -1,
                        Collectors.toSet()));

        HttpLogger.configDisabledPath(matchPathMap.get(true), matchPathMap.get(false));
    }
}
