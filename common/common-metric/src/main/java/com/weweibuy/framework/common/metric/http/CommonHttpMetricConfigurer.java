package com.weweibuy.framework.common.metric.http;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author durenhao
 * @date 2020/7/4 11:43
 **/
public interface CommonHttpMetricConfigurer {

    default String configurerNamePrefix() {
        return StringUtils.EMPTY;
    }

    default void addPathNameMapping(Map<String, String> pathNameMapping) {
    }
}
