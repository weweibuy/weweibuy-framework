
package com.weweibuy.framework.common.feign.config;

import com.weweibuy.framework.common.feign.log.FeignLogger;
import com.weweibuy.framework.common.feign.support.FeignFilter;
import com.weweibuy.framework.common.feign.support.FeignLogConfigurer;
import com.weweibuy.framework.common.feign.support.FeignLogSetting;
import com.weweibuy.framework.common.feign.support.LogFeignFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2022/10/15 11:45
 **/
@AutoConfiguration
@ConditionalOnProperty(prefix = "common.feign.log", name = "position", havingValue = "feign")
public class FeignLogConfig {

    @Autowired(required = false)
    private List<FeignLogConfigurer> feignLogConfigurerList;

    @Bean
    public FeignLogger feignLogger() {
        List<FeignLogSetting> arrayList = new ArrayList<>();
        Optional.ofNullable(feignLogConfigurerList)
                .ifPresent(l -> l.forEach(f -> f.configurer(arrayList)));
        List<FeignLogSetting> settings = arrayList.stream()
                .collect(Collectors.toList());

        return new FeignLogger(settings);
    }

    @Bean
    public FeignFilter logFeignFilter() {
        return new LogFeignFilter();
    }


}
