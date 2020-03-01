package com.weiweibuy.framework.common.log.config;

import com.weiweibuy.framework.common.log.desensitization.LogMessageConverter;
import com.weiweibuy.framework.common.log.desensitization.PatternReplaceConfig;

import java.util.Map;
import java.util.Set;

/**
 * 脱敏 映射配置
 *
 * @author durenhao
 * @date 2020/3/1 21:14
 **/
public interface SensitizationMappingConfig {

    /**
     * 增加 映射
     *
     * @param mappingMap key  ->  匹配脱敏路基   v -> 脱敏字段
     *                   {@link  LogMessageConverter}  内置部分字段处理方式
     *                   {@link PatternReplaceConfig  自定义字段处理方式}
     */
    void addSensitizationMapping(Map<String, Set<String>> mappingMap);

}
