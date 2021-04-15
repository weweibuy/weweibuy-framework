package com.weweibuy.framework.samples.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mapping.model.SnakeCaseFieldNamingStrategy;

/**
 * @author durenhao
 * @date 2021/3/18 21:57
 **/
@Configuration
public class EsConfig {

    @Bean
    public SnakeCaseFieldNamingStrategy snakeCaseFieldNamingStrategy() {
        return new SnakeCaseFieldNamingStrategy();
    }
}
