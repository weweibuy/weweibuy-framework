package com.weweibuy.framework.common.log.config;

import com.weweibuy.framework.common.log.logger.HttpLogger;
import com.weweibuy.framework.common.log.mvc.RequestLogContextFilter;
import com.weweibuy.framework.common.log.mvc.RequestResponseBodyLogAdvice;
import com.weweibuy.framework.common.log.mvc.TraceCodeFilter;
import com.weweibuy.framework.common.log.mvc.UnRequestBodyJsonLogInterceptor;
import com.weweibuy.framework.common.log.support.LogDisableConfigurer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2020/3/1 10:39
 **/
@Configuration
@EnableConfigurationProperties({CommonLogProperties.class})
@ConditionalOnProperty(prefix = "common.log", name = "enable", havingValue = "true", matchIfMissing = true)
@ConditionalOnBean(type = "org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration")
public class CommonLogConfig implements WebMvcConfigurer, InitializingBean {

    @Autowired
    private CommonLogProperties commonLogProperties;

    @Autowired(required = false)
    private List<LogDisableConfigurer> logDisableConfigurer;

    @Bean
    public RequestLogContextFilter requestLogContextFilter() {
        return new RequestLogContextFilter();
    }

    @Bean
    @ConditionalOnProperty(prefix = "common.log.trace", name = "enable", havingValue = "true", matchIfMissing = true)
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
        List<LogDisablePath> propertiesConfigurerDisablePathList = disablePath.stream()
                .map(s -> LogDisablePath.builder().path(s).type(LogDisablePath.Type.ALL).build())
                .collect(Collectors.toList());


        List<LogDisablePath> codeConfigurerDisablePathList = new ArrayList<>();

        Optional.ofNullable(logDisableConfigurer)
                .filter(CollectionUtils::isNotEmpty)
                .ifPresent(l -> l.forEach(c -> c.addHttpDisableConfig(codeConfigurerDisablePathList)));

        codeConfigurerDisablePathList.addAll(propertiesConfigurerDisablePathList);

        Map<String, LogDisablePath> disablePathMap = codeConfigurerDisablePathList.stream()
                .filter(c -> StringUtils.isNotBlank(c.getPath()))
                .peek(c -> {
                    if (c.getType() == null) {
                        c.setType(LogDisablePath.Type.ALL);
                    }
                })
                .collect(Collectors.toMap(LogDisablePath::getPath,
                        Function.identity(), (o, n) -> {
                            if (!Objects.equals(o.getType(), n.getType())) {
                                n.setType(LogDisablePath.Type.ALL);
                            }
                            return n;
                        }));

        // 对规则匹配的进行分组
        // true为匹配型 false为精确型
        Map<Boolean, Set<LogDisablePath>> matchPathMap = disablePathMap.entrySet().stream()
                .collect(Collectors.groupingBy(e -> e.getKey().indexOf('*') != -1,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toSet())));

        HttpLogger.configDisabledPath(Optional.ofNullable(matchPathMap.get(true)).orElse(Collections.emptySet()),
                Optional.ofNullable(matchPathMap.get(false)).orElse(Collections.emptySet()));
    }
}
