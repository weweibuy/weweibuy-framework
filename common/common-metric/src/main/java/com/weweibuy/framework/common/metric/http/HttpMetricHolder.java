package com.weweibuy.framework.common.metric.http;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Timer;
import lombok.Data;

/**
 * @author durenhao
 * @date 2020/7/3 23:40
 **/
@Data
public class HttpMetricHolder {

    /**
     * QT
     */
    private Timer rtTimer;

    /**
     * QPS
     */
    private Meter qpsMeter;

    /**
     * 异常比例
     */
    private Histogram exceptionRatioHistogram;

}
