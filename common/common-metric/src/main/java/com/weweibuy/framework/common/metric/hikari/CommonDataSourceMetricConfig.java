package com.weweibuy.framework.common.metric.hikari;

import com.codahale.metrics.MetricRegistry;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author durenhao
 * @date 2020/6/30 20:45
 **/
@Configuration
@ConditionalOnBean(HikariDataSource.class)
@ConditionalOnProperty(prefix = "common.metric.hikari", name = "enable", havingValue = "true", matchIfMissing = true)
public class CommonDataSourceMetricConfig implements InitializingBean {

    private final HikariDataSource dataSource;

    private final MetricRegistry metricRegistry;

    public CommonDataSourceMetricConfig(HikariDataSource dataSource, MetricRegistry metricRegistry) {
        this.dataSource = dataSource;
        this.metricRegistry = metricRegistry;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        dataSource.setMetricRegistry(metricRegistry);
    }
}
