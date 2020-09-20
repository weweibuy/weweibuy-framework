package com.weweibuy.framework.common.metric.http;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author durenhao
 * @date 2020/7/3 23:40
 **/
@Slf4j
public class HttpMetricOperator {

    private final MetricRegistry metricRegistry;

    private final String namePrefix;

    private ConcurrentMap<String, HttpMetric> patternPath;

    private ConcurrentMap<String, HttpMetric> exactPath;

    public HttpMetricOperator(MetricRegistry metricRegistry, String namePrefix, Set<String> metricPath) {
        this.metricRegistry = metricRegistry;
        this.namePrefix = namePrefix;
        Map<Boolean, ConcurrentMap<String, HttpMetric>> mapMap = groupMapping(metricPath);
        patternPath = Optional.ofNullable(mapMap.get(true))
                .orElse(new ConcurrentHashMap<>(8));
        exactPath = Optional.ofNullable(mapMap.get(false))
                .orElse(new ConcurrentHashMap<>());
    }

    public void onRequestMetric(String path, String method, long startTimeNano, int stats) {
        isMatch(path).ifPresent(m -> m.metricRequest(startTimeNano, stats));
    }

    /**
     * 请求地址是否统计
     *
     * @param path
     * @return
     */
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

    /**
     * 增加一个统计
     *
     * @param metricPath
     */
    public synchronized Collection<HttpMetric> registryHttpMetric(Set<String> metricPath) {
        Map<String, String> stringMap = metricPath.stream()
                .filter(p -> !isMatch(HttpRequestUtils.sanitizedPath(p)).isPresent())
                .collect(Collectors.toMap(p -> HttpRequestUtils.sanitizedPath(p), p -> HttpRequestUtils.sanitizedPath(p)));
        Map<Boolean, ConcurrentMap<String, HttpMetric>> concurrentMapMap = groupMapping(metricPath);
        ConcurrentMap<String, HttpMetric> metricMap = null;
        if ((metricMap = concurrentMapMap.get(true)) != null) {
            patternPath.putAll(metricMap);
        }
        if ((metricMap = concurrentMapMap.get(false)) != null) {
            exactPath.putAll(metricMap);
        }
        return concurrentMapMap.entrySet().stream()
                .flatMap(e -> e.getValue().entrySet().stream())
                .map(e -> e.getValue())
                .collect(Collectors.toList());
    }


    /**
     * 查询全部统计
     *
     * @return
     */
    public Collection<HttpMetric> listHttpMetric() {
        return Stream.concat(
                patternPath.entrySet().stream()
                        .map(Map.Entry::getValue),
                exactPath.entrySet().stream()
                        .map(Map.Entry::getValue))
                .collect(Collectors.toList());
    }

    /**
     * 移除
     *
     * @param pathCollection
     */
    public synchronized Collection<HttpMetric> removeHttpMetric(Collection<String> pathCollection) {
        return pathCollection.stream()
                .map(this::removeHttpMetric)
                .filter(Objects::nonNull)
                .peek(HttpMetric::remove)
                .collect(Collectors.toList());

    }

    private HttpMetric removeHttpMetric(String path) {
        HttpMetric httpMetric;
        return (httpMetric = patternPath.remove(path)) != null ||
                (httpMetric = exactPath.remove(path)) != null ? httpMetric : null;
    }


    private Map<Boolean, ConcurrentMap<String, HttpMetric>> groupMapping(Set<String> metricPath) {
        return metricPath.stream()
                .collect(Collectors.groupingBy(p -> p.indexOf("*") != -1,
                        Collectors.toConcurrentMap(p -> HttpRequestUtils.sanitizedPath(p),
                                p -> new HttpMetric(metricRegistry, namePrefix, HttpRequestUtils.sanitizedPath(p), HttpRequestUtils.sanitizedPath(p)))));
    }


    @Data
    static class HttpMetric {

        @JsonIgnore
        private MetricRegistry metricRegistry;

        /**
         * 路径
         */
        private String path;

        private String namePrefix;

        private String namePath;

        private String rtTimerName;

        /**
         * RT
         */
        @JsonIgnore
        private Timer rtTimer;

        private String qpsMeterName;

        /**
         * QPS
         */
        @JsonIgnore
        private Meter qpsMeter;

        private String clientExceptionMetersName;

        @JsonIgnore
        private Meter clientExceptionMeters;

        private String serverExceptionMetersName;

        @JsonIgnore
        private Meter serverExceptionMeters;

        public HttpMetric(MetricRegistry metricRegistry, String namePrefix, String path, String namePath) {
            this.metricRegistry = metricRegistry;
            this.path = path;
            this.namePrefix = namePrefix;
            this.namePath = namePath;

            this.rtTimerName = MetricRegistry.name(namePrefix, namePath, "RT");
            this.rtTimer = metricRegistry.timer(rtTimerName);

            this.qpsMeterName = MetricRegistry.name(namePrefix, namePath, "QPS");
            this.qpsMeter = metricRegistry.meter(qpsMeterName);

            this.clientExceptionMetersName = MetricRegistry.name(namePrefix, namePath, "4xxRatio");
            this.clientExceptionMeters = metricRegistry.meter(clientExceptionMetersName);

            this.serverExceptionMetersName = MetricRegistry.name(namePrefix, namePath, "5xxRatio");
            this.serverExceptionMeters = metricRegistry.meter(serverExceptionMetersName);
        }

        private void metricRequest(long startTimeNano, int stats) {
            rtTimer.update(System.nanoTime() - startTimeNano, TimeUnit.NANOSECONDS);
            qpsMeter.mark();
            qpsMeter.getFifteenMinuteRate();
            if (400 <= stats && stats < 500) {
                clientExceptionMeters.mark();
            } else if (500 <= stats) {
                serverExceptionMeters.mark();
            }
        }

        private void remove() {
            metricRegistry.remove(rtTimerName);
            metricRegistry.remove(qpsMeterName);
            metricRegistry.remove(clientExceptionMetersName);
            metricRegistry.remove(serverExceptionMetersName);
        }


    }


}
