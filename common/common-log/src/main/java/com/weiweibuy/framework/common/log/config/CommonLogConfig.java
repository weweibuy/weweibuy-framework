package com.weiweibuy.framework.common.log.config;

import com.weiweibuy.framework.common.log.mvc.RequestLogContextFilter;
import com.weiweibuy.framework.common.log.mvc.RequestResponseBodyLogAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author durenhao
 * @date 2020/3/1 10:39
 **/
@Configuration
@ConditionalOnProperty(prefix = "common.log", name = "enable", havingValue = "true", matchIfMissing = true)
public class CommonLogConfig {

    @Autowired(required = false)
    private List<SensitizationMappingConfig> mappingConfigList;

    @Bean
    public RequestLogContextFilter requestLogContextFilter() {
        Map<String, Set<String>> setHashMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(mappingConfigList)) {
            mappingConfigList.forEach(c -> c.addSensitizationMapping(setHashMap));
        }
        return new RequestLogContextFilter(setHashMap);
    }

    @Bean
    public RequestResponseBodyLogAdvice requestResponseBodyLogAdvice() {
        return new RequestResponseBodyLogAdvice();
    }


}
