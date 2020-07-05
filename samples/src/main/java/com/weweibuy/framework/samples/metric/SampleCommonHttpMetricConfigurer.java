package com.weweibuy.framework.samples.metric;

import com.weweibuy.framework.common.metric.http.CommonHttpMetricConfigurer;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author durenhao
 * @date 2020/7/4 11:57
 **/
@Component
public class SampleCommonHttpMetricConfigurer implements CommonHttpMetricConfigurer {

    @Override
    public void addMetricPath(Set<String> pathSet) {
        pathSet.add("/hello");
    }
}
