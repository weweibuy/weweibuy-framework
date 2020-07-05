package com.weweibuy.framework.common.metric.http;

import com.weweibuy.framework.common.metric.MeasurementMappingConfigurer;

import java.util.Set;

/**
 * HTTP 接口统计配置
 *
 * @author durenhao
 * @date 2020/7/4 11:43
 **/
public interface CommonHttpMetricConfigurer extends MeasurementMappingConfigurer {

    /**
     * 增加统计path
     *
     * @param pathSet
     */
    default void addMetricPath(Set<String> pathSet) {
    }


}
