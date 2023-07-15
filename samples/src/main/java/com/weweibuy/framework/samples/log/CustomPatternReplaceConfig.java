package com.weweibuy.framework.samples.log;


import com.weweibuy.framework.common.log.desensitization.DesensitizationLogMessageConverter;
import com.weweibuy.framework.common.log.desensitization.PatternReplaceConfig;

import java.util.Map;

/**
 * @author durenhao
 * @date 2020/3/1 23:37
 **/
public class CustomPatternReplaceConfig implements PatternReplaceConfig {

    @Override
    public void addPatternReplace(Map<String, DesensitizationLogMessageConverter.PatternReplace> patternReplaceMap) {
    }

}
