package com.weweibuy.framework.compensate.core;

import com.weweibuy.framework.compensate.interfaces.CompensateConfigStore;
import com.weweibuy.framework.compensate.interfaces.model.CompensateConfigProperties;
import com.weweibuy.framework.compensate.support.BuiltInCompensateType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author durenhao
 * @date 2020/2/14 22:42
 **/
@ConfigurationProperties
@EnableConfigurationProperties(SimpleCompensateConfigStore.class)
public class SimpleCompensateConfigStore implements CompensateConfigStore, InitializingBean {

    private static final String DEFAULT_KEY = "default";

    private Map<String, CompensateConfigProperties> compensate = new HashMap<>();

    private CompensateConfigProperties defaultProperties;

    @Override
    public CompensateConfigProperties compensateConfig(String compensateKey) {
        CompensateConfigProperties compensateConfigProperties = compensate.get(compensateKey);
        if (compensateConfigProperties == null) {
            return defaultProperties;
        }
        return compensateConfigProperties;
    }

    public Map<String, CompensateConfigProperties> getCompensate() {
        return compensate;
    }

    public void setCompensate(Map<String, CompensateConfigProperties> compensate) {
        this.compensate = compensate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (compensate.get(DEFAULT_KEY) == null) {
            CompensateConfigProperties properties = new CompensateConfigProperties();
            properties.setCompensateType(BuiltInCompensateType.METHOD_ARGS.toString());
            properties.setRetryRule("10s 30s 1m");
            properties.setAlarmRule("30s 1m 2m");
            compensate.put(DEFAULT_KEY, properties);
            defaultProperties = properties;
        }

        compensate.values().stream()
                .forEach(v -> {
                    if (StringUtils.isEmpty(v.getCompensateType())) {
                        v.setCompensateType(BuiltInCompensateType.METHOD_ARGS.toString());
                    }
                    if (StringUtils.isEmpty(v.getAlarmRule())) {
                        v.setAlarmRule(defaultProperties.getAlarmRule());
                    }
                    if (StringUtils.isEmpty(v.getRetryRule())) {
                        v.setRetryRule(defaultProperties.getRetryRule());
                    }
                });
    }
}
