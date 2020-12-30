package com.weweibuy.framework.common.core.config;

import com.weweibuy.framework.common.core.support.AlarmService;
import com.weweibuy.framework.common.core.support.LogAlarmService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 报警配置
 *
 * @author durenhao
 * @date 2020/12/30 21:24
 **/
@Configuration
public class CommonAlarmConfig {

    @Bean
    @ConditionalOnMissingBean(AlarmService.class)
    public AlarmService alarmService() {
        return new LogAlarmService();
    }


}
