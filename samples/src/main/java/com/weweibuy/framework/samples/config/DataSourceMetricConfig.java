package com.weweibuy.framework.samples.config;

import com.codahale.metrics.MetricRegistry;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author durenhao
 * @date 2020/6/30 20:45
 **/
@Configuration
public class DataSourceMetricConfig implements InitializingBean {

    private final HikariDataSource dataSource;

    private final MetricRegistry metricRegistry;

    public DataSourceMetricConfig(HikariDataSource dataSource, MetricRegistry metricRegistry) {
        this.dataSource = dataSource;
        this.metricRegistry = metricRegistry;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        dataSource.setMetricRegistry(metricRegistry);
    }
}
