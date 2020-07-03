package com.weweibuy.framework.common.metric.http;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2020/7/3 21:07
 **/
@Order(-100)
public class HttpMetricFilter extends OncePerRequestFilter {

    private final MetricRegistry metricRegistry;

    private Map<String, String> patternPath;

    private Map<String, String> exactPath;

    private final Timer rtTimer;

    public HttpMetricFilter(MetricRegistry metricRegistry, HttpMetricProperties httpMetricProperties) {
        this.metricRegistry = metricRegistry;
        init(httpMetricProperties);
        this.rtTimer = metricRegistry.timer(MetricRegistry.name("poolName", "METRIC_CATEGORY", "METRIC_NAME_WAIT"));
    }

    private void init(HttpMetricProperties properties) {
        Map<Boolean, Map<String, String>> mapMap = properties.getPathMapping().entrySet().stream()
                .collect(Collectors.groupingBy(e -> e.getKey().indexOf("*") != -1,
                        Collectors.toMap(e -> e.getKey(), e -> e.getValue())));
        patternPath = Optional.ofNullable(mapMap.get(true))
                .orElse(Collections.emptyMap());
        exactPath = Optional.ofNullable(mapMap.get(false))
                .orElse(Collections.emptyMap());
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        long startTime = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            rtTimer.update(System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS);
        }

    }


}
