package com.weweibuy.framework.common.log.desensitization;

import java.util.Map;

/**
 * JDK java.util.ServiceLoader 加载 自定配置
 * 需要 classPath/META-INF/services/  目录下自定义实现
 *
 * @author durenhao
 * @date 2020/3/1 23:30
 **/
public interface PatternReplaceConfig {

    /**
     * 增加 字段处方式  {@link DesensitizationLogMessageConverter}
     *
     * @param patternReplaceMap
     */
    default void addPatternReplace(Map<String, DesensitizationLogMessageConverter.PatternReplace> patternReplaceMap) {
        // do nothing
    }

    /**
     * 增加脱敏规则配置
     *
     * @param configurer {@link  DesensitizationLogMessageConverter}  内置部分字段处理方式
     */
    void addDesensitizationRule(SensitizationMappingConfigurer configurer);
}
