package com.weweibuy.framework.common.core.config;

import com.weweibuy.framework.common.core.support.AlarmService;
import com.weweibuy.framework.common.core.support.ApplicationNameSystemIdGetter;
import com.weweibuy.framework.common.core.support.LogAlarmService;
import com.weweibuy.framework.common.core.support.SystemIdGetter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author durenhao
 * @date 2021/5/17 22:37
 **/
@Configuration
public class CommonCoreConfig {

    @Bean
    @ConditionalOnMissingBean(AlarmService.class)
    public AlarmService alarmService() {
        return new LogAlarmService();
    }

    @Bean
    @ConditionalOnMissingBean(SystemIdGetter.class)
    public SystemIdGetter systemIdGetter(Environment environment) {
        return new ApplicationNameSystemIdGetter(environment);
    }


}
