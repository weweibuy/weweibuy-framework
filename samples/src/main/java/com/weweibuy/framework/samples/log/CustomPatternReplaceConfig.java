package com.weweibuy.framework.samples.log;

import com.weiweibuy.framework.common.log.desensitization.LogMessageConverter;
import com.weiweibuy.framework.common.log.desensitization.PatternReplaceConfig;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author durenhao
 * @date 2020/3/1 23:37
 **/
public class CustomPatternReplaceConfig implements PatternReplaceConfig {

    @Override
    public void addPatternReplace(Map<String, LogMessageConverter.PatternReplace> patternReplaceMap) {
        patternReplaceMap.put("123", new LogMessageConverter.PatternReplace(Pattern.compile("xxx"), "xxx"));
    }
}
