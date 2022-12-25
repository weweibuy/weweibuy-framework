package com.weweibuy.framework.common.metric.config;

import com.weweibuy.framework.common.metric.support.MeterInstanceGetter;
import com.weweibuy.framework.common.metric.support.SampleMeterInstanceGetter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * 通用的 Meter 配置
 *
 * @author durenhao
 * @date 2021/7/1 22:45
 **/
@AutoConfiguration
@RequiredArgsConstructor
public class CommonMeterConfig {


    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(MeterInstanceGetter meterInstanceGetter) {
        String instance = meterInstanceGetter.getInstance();
        return registry -> registry.config()
                .commonTags("instance", instance);
    }

    @Bean
    @ConditionalOnMissingBean
    public MeterInstanceGetter meterInstanceGetter(Environment environment) {
        return new SampleMeterInstanceGetter(environment);
    }

}
