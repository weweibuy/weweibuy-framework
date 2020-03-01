package com.weweibuy.framework.samples.log;

import com.weiweibuy.framework.common.log.config.SensitizationMappingConfig;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author durenhao
 * @date 2020/3/1 22:30
 **/
@Component
public class RequestSensitizationMappingConfig implements SensitizationMappingConfig {

    @Override
    public void addSensitizationMapping(Map<String, Set<String>> mappingMap) {
        mappingMap.put("/hello_GET", Collections.singleton("phone"));
        Set<String> set = new HashSet<>();
        set.add("phone");
        set.add("password");
        mappingMap.put("/hello_POST", set);
    }
}
