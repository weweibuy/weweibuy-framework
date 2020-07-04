package com.weweibuy.framework.common.metric.http;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import lombok.Data;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2020/7/3 23:40
 **/
@Data
public class HttpMetricOperator {

    private Map<String, HttpMetric> patternPath;

    private Map<String, HttpMetric> exactPath;

    public HttpMetricOperator(MetricRegistry metricRegistry, String namePrefix, Map<String, String> mappingMap) {
        Map<Boolean, Map<String, HttpMetric>> mapMap = mappingMap.entrySet().stream()
                .collect(Collectors.groupingBy(e -> e.getKey().indexOf("*") != -1,
                        Collectors.toMap(e -> e.getKey(), e -> new HttpMetric(metricRegistry, namePrefix, e.getValue()))));
        patternPath = Optional.ofNullable(mapMap.get(true))
                .orElse(Collections.emptyMap());
        exactPath = Optional.ofNullable(mapMap.get(false))
                .orElse(Collections.emptyMap());
    }

    public void onRequestMetric(String path, String method, long startTimeNano, int stats) {
        isMatch(path).ifPresent(m -> m.metricRequest(startTimeNano, stats));
    }


    private Optional<HttpMetric> isMatch(String path) {
        HttpMetric httpMetric = null;
        if (!exactPath.isEmpty() && (httpMetric = exactPath.get(path)) != null) {
            return Optional.ofNullable(httpMetric);
        }

        if (!patternPath.isEmpty()) {
            return patternPath.entrySet().stream()
                    .filter(e -> HttpRequestUtils.isMatchPath(e.getKey(), path))
                    .map(Map.Entry::getValue)
                    .findFirst();
        }
        return Optional.empty();
    }


    @Data
    static class HttpMetric {

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

        public HttpMetric(MetricRegistry metricRegistry, String namePrefix, String namePath) {
            this.rtTimer = metricRegistry.timer(MetricRegistry.name(namePrefix, namePath, "RT"));
            this.qpsMeter = metricRegistry.meter(MetricRegistry.name(namePrefix, namePath, "QPS"));
            this.exceptionRatioHistogram = metricRegistry.histogram(MetricRegistry.name(namePrefix, namePath, "ExceptionRatio"));
        }

        private void metricRequest(long startTimeNano, int stats) {
            rtTimer.update(System.nanoTime() - startTimeNano, TimeUnit.NANOSECONDS);
            qpsMeter.mark();
            exceptionRatioHistogram.update(stats);
        }
    }


}
