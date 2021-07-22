package com.weweibuy.framework.common.core.config;

import com.weweibuy.framework.common.core.config.properties.CommonJacksonProperties;
import com.weweibuy.framework.common.core.support.JacksonBuilderHelper;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * jackson 配置
 *
 * @author durenhao
 * @date 2020/3/1 10:57
 **/
@Configuration
@EnableConfigurationProperties({CommonJacksonProperties.class})
@RequiredArgsConstructor
public class JackJsonConfig {

    private final CommonJacksonProperties commonJacksonProperties;


    @Bean
    public JackJsonUtils jackJsonUtils() {
        return new JackJsonUtils();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer localDateTimeJackson2ObjectMapperBuilderCustomizer() {
        return JacksonBuilderHelper.localDateTimeCustomizer(commonJacksonProperties.getLocalDateTimeFormat());
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer localDateJackson2ObjectMapperBuilderCustomizer() {
        return JacksonBuilderHelper.localDateCustomizer(commonJacksonProperties.getLocalDateFormat());
    }


}
