package com.weweibuy.framework.common.metric;

import com.codahale.metrics.MetricRegistry;
import com.izettle.metrics.influxdb.InfluxDbReporter;
import com.weweibuy.framework.common.metric.hikari.CommonDataSourceMetricConfig;
import com.weweibuy.framework.common.metric.http.CommonHttpMetricConfig;
import com.weweibuy.framework.common.metric.influxdb.InfluxDbHttpClientSender;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author durenhao
 * @date 2020/6/30 20:56
 **/
@Configuration
@Import(value = {CommonDataSourceMetricConfig.class, CommonHttpMetricConfig.class})
@EnableConfigurationProperties(MetricInfluxDbProperties.class)
public class CommonMetricConfig {

    @Autowired(required = false)
    private List<MeasurementMappingConfigurer> measurementMappingConfigurerList;

    @Autowired
    private MetricInfluxDbProperties metricInfluxDbProperties;

    @Autowired
    private CloseableHttpClient closeableHttpClient;

    @Bean
    public MetricRegistry metricRegistry(Environment environment) throws Exception {
        MetricRegistry metricRegistry = new MetricRegistry();
        startReporter(metricRegistry);
        return metricRegistry;
    }


    private void startReporter(MetricRegistry metricRegistry) throws Exception {
        InfluxDbHttpClientSender httpClientSender = new InfluxDbHttpClientSender(metricInfluxDbProperties, closeableHttpClient);

        String hostName = InetAddress.getLocalHost().getHostName();
        InfluxDbReporter.Builder builder = InfluxDbReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS);

        if (CollectionUtils.isNotEmpty(measurementMappingConfigurerList)) {
            Map<String, String> measurementMapping = new HashMap<>();
            TransformerComposite transformerComposite = new TransformerComposite();
            transformerComposite.addTagTransformer(name -> Collections.singletonMap("server", hostName));
            measurementMappingConfigurerList.stream()
                    .peek(c -> c.configurerMeasurementMappings(measurementMapping))
                    .forEach(c -> c.configurerTagTransformer(transformerComposite));
            builder.measurementMappings(measurementMapping)
                    .tagsTransformer(transformerComposite);
        }

        InfluxDbReporter reporter = builder.build(httpClientSender);
        reporter.start(10, TimeUnit.SECONDS);
    }


}
