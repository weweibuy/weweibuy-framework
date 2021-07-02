package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.common.metric.http.HttpMetricOperator;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.Set;

/**
 * @author durenhao
 * @date 2020/7/5 12:18
 **/
@RestController
@RequestMapping("/http-metric")
@RequiredArgsConstructor
public class HttpMetricController {

    private final MeterRegistry meterRegistry;

    private final HttpMetricOperator httpMetricOperator;


    @PostMapping("/add")
    public Object addMetric(@RequestBody @Valid Set<String> nameMappingSet) {
        if (CollectionUtils.isEmpty(nameMappingSet)) {
            return Collections.emptyList();
        }
        return httpMetricOperator.registryHttpMetric(nameMappingSet);
    }

    @PostMapping("/remove")
    public Object removeMetric(@RequestBody Set<String> pathSet) {
        if (CollectionUtils.isEmpty(pathSet)) {
            return Collections.emptyList();
        }
        return httpMetricOperator.removeHttpMetric(pathSet);
    }

    @GetMapping("/list")
    public Object listMetric() {
        return httpMetricOperator.listHttpMetric();
    }

    @Data
    public static class MetricNameMapping {

        @NotBlank
        private String path;

        @NotBlank
        private String name;
    }


}
