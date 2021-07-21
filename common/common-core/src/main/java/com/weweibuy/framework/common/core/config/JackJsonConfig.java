package com.weweibuy.framework.common.core.config;

import com.weweibuy.framework.common.core.support.JacksonBuilderHelper;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * jackson 配置
 *
 * @author durenhao
 * @date 2020/3/1 10:57
 **/
@Configuration
public class JackJsonConfig {


    @Bean
    public JackJsonUtils jackJsonUtils() {
        return new JackJsonUtils();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer localDateTimeJackson2ObjectMapperBuilderCustomizer() {
        return JacksonBuilderHelper.localDateTimeCustomizer();

    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer localDateJackson2ObjectMapperBuilderCustomizer() {
        return JacksonBuilderHelper.localDateCustomizer();
    }


}
