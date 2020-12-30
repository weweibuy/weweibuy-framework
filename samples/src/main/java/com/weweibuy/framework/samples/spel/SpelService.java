package com.weweibuy.framework.samples.spel;

import com.weweibuy.framework.common.core.expression.CommonCachedExpressionEvaluator;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * @author durenhao
 * @date 2020/8/2 10:55
 **/
@Service
public class SpelService extends CommonCachedExpressionEvaluator {

    public static void main(String[] args) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
    }

}
