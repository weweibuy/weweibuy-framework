package com.weweibuy.framework.common.metric.http;

import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2020/6/30 20:45
 **/
@Configuration
@ConditionalOnProperty(prefix = "common.metric.http", name = "enable", havingValue = "true", matchIfMissing = true)
public class CommonHttpMetricConfig {

    @Autowired(required = false)
    private List<CommonHttpMetricConfigurer> commonHttpMetricConfigurers;


    @Bean
    public HttpMetricOperator commonHttpMetricOperator(MetricRegistry metricRegistry, Environment environment) {
        Optional<String> namePrefixOpt = Optional.empty();
        Map<String, String> pathNameMappingMap = new HashMap<>();
        if (commonHttpMetricConfigurers != null) {
            namePrefixOpt = commonHttpMetricConfigurers.stream()
                    .peek(c -> c.addPathNameMapping(pathNameMappingMap))
                    .findFirst()
                    .map(CommonHttpMetricConfigurer::configurerNamePrefix);
        }
        String namePrefix = namePrefixOpt.orElse(environment.getProperty("spring.application.name"));
        return new HttpMetricOperator(metricRegistry, namePrefix, pathNameMappingMap);
    }

    @Bean
    public HttpMetricFilter commonHttpMetricFilter(MetricRegistry metricRegistry, Environment environment) {
        return new HttpMetricFilter(commonHttpMetricOperator(metricRegistry, environment));
    }

}
