package com.weweibuy.framework.compensate.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author durenhao
 * @date 2020/2/14 22:42
 **/
@ConfigurationProperties
@EnableConfigurationProperties(SimpleCompensateConfigStore.class)
public class SimpleCompensateConfigStore implements CompensateConfigStore {

    private Map<String, CompensateConfigProperties> compensate = new HashMap<>();

    @Override
    public CompensateConfigProperties compensateConfig(String compensateKey) {
        return compensate.get(compensateKey);
    }

    public Map<String, CompensateConfigProperties> getCompensate() {
        return compensate;
    }

    public void setCompensate(Map<String, CompensateConfigProperties> compensate) {
        this.compensate = compensate;
    }
}
