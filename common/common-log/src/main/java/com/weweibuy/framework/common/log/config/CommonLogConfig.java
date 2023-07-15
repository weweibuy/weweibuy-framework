package com.weweibuy.framework.common.log.config;

import com.weweibuy.framework.common.log.mvc.*;
import com.weweibuy.framework.common.log.support.HttpLogConfigurer;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志相关配置
 *
 * @author durenhao
 * @date 2020/3/1 10:39
 **/
@AutoConfiguration
@EnableConfigurationProperties({CommonLogProperties.class})
@ConditionalOnProperty(prefix = "common.log", name = "enable", havingValue = "true", matchIfMissing = true)
@ConditionalOnBean(type = "org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration")
@ConditionalOnClass(name = "org.springframework.web.servlet.config.annotation.WebMvcConfigurer")
public class CommonLogConfig {

    @Autowired
    private CommonLogProperties commonLogProperties;

    @Autowired(required = false)
    private List<HttpLogConfigurer> logDisableConfigurer;

    @Bean
    @ConditionalOnProperty(prefix = "common.log.trace", name = "enable", havingValue = "true", matchIfMissing = true)
    public TraceCodeFilter traceCodeFilter() {
        return new TraceCodeFilter();
    }

    @Bean
    @ConditionalOnProperty(prefix = "common.log.sensitization", name = "enable", havingValue = "true")
    public SensitizationFilter sensitizationFilter() {
        return new SensitizationFilter(mvcPathMappingOperator());
    }

    @Bean
    public MvcLogRequestHandler mvcLogRequestHandler() {
        return new MvcLogRequestHandler(mvcPathMappingOperator());
    }

    @Bean
    public MvcLogResponseHandler mvcLogResponseHandler() {
        return new MvcLogResponseHandler(mvcPathMappingOperator());
    }

    @Bean
    public MvcPathMappingOperator mvcPathMappingOperator() {
        List<CommonLogProperties.HttpPathProperties> codeLogHttpProperties = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(logDisableConfigurer)) {
            logDisableConfigurer.forEach(l -> l.addHttpLogConfig(codeLogHttpProperties));
        }
        List<CommonLogProperties.HttpPathProperties> httpPath = commonLogProperties.getHttpPath();
        return new MvcPathMappingOperator(httpPath, codeLogHttpProperties);
    }

}
