package com.weweibuy.framework.samples.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author durenhao
 * @date 2020/9/26 18:57
 **/
@Configuration
public class HttpsConfig {

    @Bean
    public HttpServletWebServerFactoryCustomizer httpServletWebServerFactoryCustomizer() {
        return new HttpServletWebServerFactoryCustomizer();
    }


}
