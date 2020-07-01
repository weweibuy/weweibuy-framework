package com.weweibuy.framework.samples.config;

import com.codahale.metrics.MetricRegistry;
import com.izettle.metrics.influxdb.InfluxDbHttpSender;
import com.izettle.metrics.influxdb.InfluxDbReporter;
import com.izettle.metrics.influxdb.InfluxDbTcpSender;
import com.izettle.metrics.influxdb.InfluxDbUdpSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author durenhao
 * @date 2020/6/30 20:56
 **/
@Configuration
public class MetricConfig {

    @Bean
    public MetricRegistry metricRegistry() throws Exception {

        InfluxDbTcpSender influxDbTcpSender = new InfluxDbTcpSender("106.12.208.53",
                8086, 2000, "java_metric",
                "metric");

        InfluxDbHttpSender influxDbHttpSender = new InfluxDbHttpSender(
                "http", "106.12.208.53", 8086, "java_metric", "",
                TimeUnit.MILLISECONDS);

        InfluxDbUdpSender influxDbUdpSender = new InfluxDbUdpSender("106.12.208.53",
                8089, 2000, "java_metric",
                "");

        MetricRegistry metricRegistry = new MetricRegistry();
        InfluxDbReporter reporter = InfluxDbReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build(influxDbHttpSender);

        reporter.start(10, TimeUnit.SECONDS);
        return metricRegistry;
    }
}
