package com.weweibuy.framework.common.mvc.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author durenhao
 * @date 2023/2/19 17:22
 **/
@AutoConfiguration
public class CommonMvcLogConfig {

    @Bean
    public HealthLogDisableConfigurer healthLogDisableConfigurer() {
        return new HealthLogDisableConfigurer();
    }


}
