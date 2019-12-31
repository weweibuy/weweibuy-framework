/*
 * All rights Reserved, Designed By baowei
 *
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.core.RocketMethodMetadata;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 表达式 messageKey 生成器
 *
 * @author durenhao
 * @date 2019/12/31 17:37
 **/
@EnableCaching
public class ExpressionMessageGenerator implements MessageKeyGenerator {

    private SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

    private Map<String, Expression> expressionMap = new ConcurrentHashMap<>();



    @Override
    public String generatorKey(RocketMethodMetadata metadata, Object... args) {
        return null;
    }

    public EvaluationContext createEvaluationContext() {
        return null;
    }

}
