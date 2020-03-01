package com.weiweibuy.framework.common.log.desensitization;

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
     * 增加 字段处方式  {@link LogMessageConverter}
     *
     * @param patternReplaceMap
     */
    void addPatternReplace(Map<String, LogMessageConverter.PatternReplace> patternReplaceMap);


}
