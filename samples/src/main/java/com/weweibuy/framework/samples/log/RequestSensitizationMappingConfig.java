package com.weweibuy.framework.samples.log;

import com.weweibuy.framework.common.log.config.SensitizationMappingConfig;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author durenhao
 * @date 2020/3/1 22:30
 **/
@Component
public class RequestSensitizationMappingConfig implements SensitizationMappingConfig {

    // TODO builder 形式直接构建
    @Override
    public void addSensitizationMapping(Map<String, Set<String>> mappingMap) {
        HashSet<String> objects = new HashSet<>();
        objects.add("certId");
        objects.add("phone");
        objects.add("password");
        objects.add("fullName");
        objects.add("address");
        mappingMap.put("/hello_GET", objects);

        Set<String> set = new HashSet<>();
        set.add("phone");
        set.add("password");
        mappingMap.put("/hello_POST", set);
    }
}
