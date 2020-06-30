package com.weweibuy.framework.samples.controller;

import com.codahale.metrics.MetricRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author durenhao
 * @date 2020/6/30 20:57
 **/
@RestController
@RequestMapping("/data-source-metric")
public class DataSourceMetricController {

    private final MetricRegistry metricRegistry;

    public DataSourceMetricController(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @GetMapping("/gauge")
    public Object getGauge(String name){
       return metricRegistry.getGauges();
    }

    @GetMapping("/counter")
    public Object getCounters(String name){
        return metricRegistry.getCounters();
    }

    @GetMapping("/meter")
    public Object getMeters(String name){
        return metricRegistry.getMeters();
    }

    @GetMapping("/histogram")
    public Object getHistograms(String name){
        return metricRegistry.getHistograms();
    }

    @GetMapping("/timer")
    public Object getTimers(String name){
        return metricRegistry.getTimers();
    }
}
