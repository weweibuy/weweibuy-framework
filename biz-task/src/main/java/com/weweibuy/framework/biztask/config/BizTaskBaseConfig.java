package com.weweibuy.framework.biztask.config;

import com.weweibuy.framework.biztask.core.BizTaskHandlerMethodHolder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author durenhao
 * @date 2024/1/19 21:13
 **/
@AutoConfiguration
public class BizTaskBaseConfig {


    @Bean
    public BizTaskHandlerMethodHolder bizTaskHandlerMethodHolder() {
        return new BizTaskHandlerMethodHolder();
    }



}
