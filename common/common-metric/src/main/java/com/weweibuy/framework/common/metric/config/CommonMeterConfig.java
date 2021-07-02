package com.weweibuy.framework.common.metric.config;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

/**
 * 通用的 Meter 配置
 *
 * @author durenhao
 * @date 2021/7/1 22:45
 **/
@Configuration
@RequiredArgsConstructor
public class CommonMeterConfig {

    private final Environment environment;

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        String hostAddress = address.getHostAddress() + ":" + Optional.ofNullable(environment.getProperty("server.port")).orElse("8080");
        return registry -> registry.config()
                .commonTags("instance", hostAddress);
    }

}
