package com.weweibuy.framework.compensate.core;

import com.weweibuy.framework.compensate.model.BuiltInCompensateType;
import com.weweibuy.framework.compensate.model.CompensateConfigProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2020/2/14 22:42
 **/
@ConfigurationProperties(prefix = "compensate")
@EnableConfigurationProperties(SimpleCompensateConfigStore.class)
public class SimpleCompensateConfigStore implements CompensateConfigStore, InitializingBean {

    private static final String DEFAULT_KEY = "default";

    private static final String DEFAULT_RETRY_RULE = "10s 30s 1m";

    private static final String DEFAULT_ALARM_RULE = "30s 1m 2m";

    private Map<String, CompensateConfigProperties> config = new HashMap<>();

    private Integer triggerLimit = 200;

    private CompensateConfigProperties defaultProperties;

    @Override
    public CompensateConfigProperties compensateConfig(String compensateKey) {
        CompensateConfigProperties compensateConfigProperties = config.get(compensateKey);
        if (compensateConfigProperties == null) {
            return defaultProperties;
        }
        return compensateConfigProperties;
    }

    @Override
    public Integer getTriggerLimit() {
        return Optional.ofNullable(triggerLimit).orElse(200);
    }

    public Map<String, CompensateConfigProperties> getCompensate() {
        return config;
    }

    public void setCompensate(Map<String, CompensateConfigProperties> compensate) {
        this.config = compensate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        if (config.get(DEFAULT_KEY) == null) {
            CompensateConfigProperties properties = new CompensateConfigProperties();
            properties.setCompensateType(BuiltInCompensateType.METHOD_ARGS.toString());
            properties.setRetryRule(DEFAULT_RETRY_RULE);
            properties.setAlarmRule(DEFAULT_ALARM_RULE);
            config.put(DEFAULT_KEY, properties);
            defaultProperties = properties;
        }

        config.values().stream()
                .forEach(v -> {
                    if (StringUtils.hasLength(v.getCompensateType())) {
                        v.setCompensateType(BuiltInCompensateType.METHOD_ARGS.toString());
                    }
                    if (StringUtils.hasLength(v.getAlarmRule())) {
                        v.setAlarmRule(defaultProperties.getAlarmRule());
                    }
                    if (StringUtils.hasLength(v.getRetryRule())) {
                        v.setRetryRule(defaultProperties.getRetryRule());
                    }
                });
    }
}
