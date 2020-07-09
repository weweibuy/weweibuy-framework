package com.weweibuy.framework.common.metric;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * @author durenhao
 * @date 2020/7/5 20:49
 **/
@Data
@ConfigurationProperties(prefix = "common.metric.influx-db")
public class MetricInfluxDbProperties {

    private String host;

    private String database;

    private String authString;

    private Integer connectionTimeout = 200;

    private Integer socketTimeout = 500;

    private TimeUnit timePrecision = TimeUnit.MILLISECONDS;

}
