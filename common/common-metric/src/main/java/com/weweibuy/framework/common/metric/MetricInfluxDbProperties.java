package com.weweibuy.framework.common.metric;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author durenhao
 * @date 2020/7/5 20:49
 **/
@Data
@ConfigurationProperties(prefix = "common.metric.influx")
public class MetricInfluxDbProperties {

    private String host;

    private String database;

}
