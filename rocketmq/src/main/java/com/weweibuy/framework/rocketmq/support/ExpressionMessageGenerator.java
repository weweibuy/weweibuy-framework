package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.core.RocketMethodMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;
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

    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();


    @Override
    public String generatorKey(RocketMethodMetadata metadata, Object... args) {
        Expression expression = expressionMap.putIfAbsent(metadata.getKeyExpression(), spelExpressionParser.parseExpression(metadata.getKeyExpression()));
        Object value = expression.getValue(createEvaluationContext(metadata, args));
        return value.toString();
    }


    private EvaluationContext createEvaluationContext(RocketMethodMetadata metadata, Object... args) {
        ProviderMethodRootObject object = new ProviderMethodRootObject(metadata.getMethod(), args);
        return new MethodBasedEvaluationContext(object, metadata.getMethod(), args, parameterNameDiscoverer);
    }

    @Data
    @AllArgsConstructor
    public static class ProviderMethodRootObject {

        private final Method method;

        private final Object[] args;

    }


}
