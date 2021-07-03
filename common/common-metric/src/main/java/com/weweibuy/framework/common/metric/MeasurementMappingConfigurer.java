package com.weweibuy.framework.common.metric;

import java.util.Map;

/**
 * measurement 与 FluxDB MetricName 映射关系配置
 *
 * @author durenhao
 * @date 2020/7/5 21:01
 **/
public interface MeasurementMappingConfigurer {

    /**
     * measurement 与 FluxDB MetricName 映射关系
     *
     * @param measurementPatternMetricNameMapping k -> measurement
     *                                            v -> metricName 可以使用正则表达式
     */
    default void configurerMeasurementMappings(Map<String, String> measurementPatternMetricNameMapping) {
    }



}
