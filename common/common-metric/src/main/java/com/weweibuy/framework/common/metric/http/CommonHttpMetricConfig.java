package com.weweibuy.framework.common.metric.http;

import com.codahale.metrics.MetricRegistry;
import com.weweibuy.framework.common.metric.TransformerComposite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author durenhao
 * @date 2020/6/30 20:45
 **/
@Configuration
@ConditionalOnProperty(prefix = "common.metric.http", name = "enable", havingValue = "true", matchIfMissing = true)
public class CommonHttpMetricConfig implements CommonHttpMetricConfigurer {

    @Autowired(required = false)
    private List<CommonHttpMetricConfigurer> commonHttpMetricConfigurers;

    @Autowired
    private MetricRegistry metricRegistry;

    private String applicationName;
    private String clientRatioPattern;
    private String serverRatioPattern;
    private String qpsPattern;
    private String rtPattern;
    private String prefixPattern;

    public CommonHttpMetricConfig(Environment environment) {
        applicationName = environment.getProperty("spring.application.name");
        clientRatioPattern = applicationName + "\\.http\\.(.*?)(\\.4xxRatio)";
        serverRatioPattern = applicationName + "\\.http\\.(.*?)(\\.5xxRatio)";
        qpsPattern = applicationName + "\\.http\\.(.*?)(\\.QPS)";
        rtPattern = applicationName + "\\.http\\.(.*?)(\\.RT)";
        prefixPattern = applicationName + ".http";
    }

    @Bean
    public HttpMetricOperator commonHttpMetricOperator() {
        Set<String> pathSet = new HashSet<>();
        if (commonHttpMetricConfigurers != null) {
            commonHttpMetricConfigurers.stream()
                    .forEach(c -> c.addMetricPath(pathSet));
        }
        return new HttpMetricOperator(metricRegistry, prefixPattern, pathSet);
    }

    @Bean
    public HttpMetricFilter commonHttpMetricFilter() {
        return new HttpMetricFilter(commonHttpMetricOperator());
    }

    @Override
    public void configurerMeasurementMappings(Map<String, String> measurementPatternMetricNameMapping) {
        measurementPatternMetricNameMapping.put(MetricRegistry.name(prefixPattern, "4xxRatio"), clientRatioPattern);
        measurementPatternMetricNameMapping.put(MetricRegistry.name(prefixPattern, "5xxRatio"), serverRatioPattern);
        measurementPatternMetricNameMapping.put(MetricRegistry.name(prefixPattern, "QPS"), qpsPattern);
        measurementPatternMetricNameMapping.put(MetricRegistry.name(prefixPattern, "RT"), rtPattern);
    }

    @Override
    public void configurerTagTransformer(TransformerComposite composite) {
        List<Pattern> arrayList = new ArrayList<>();
        arrayList.add(Pattern.compile(clientRatioPattern));
        arrayList.add(Pattern.compile(serverRatioPattern));
        arrayList.add(Pattern.compile(qpsPattern));
        arrayList.add(Pattern.compile(rtPattern));

        composite.addTagTransformer(name ->
                arrayList.stream()
                        .map(c -> c.matcher(name))
                        .filter(Matcher::find)
                        .findFirst()
                        .map(m -> Collections.singletonMap("path", m.group(1)))
                        .orElse(Collections.emptyMap()));
    }
}
