package com.weweibuy.framework.common.core.expression;

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用的EL 表达式解析器
 *
 * @author durenhao
 * @date 2020/6/19 23:16
 **/
public abstract class CommonCachedExpressionEvaluator extends CachedExpressionEvaluator {

    private static final Map<ExpressionKey, Expression> EXPRESSION_CACHE = new ConcurrentHashMap<>();


    public String evaluatorExpressionStr(@NonNull String expressionStr, Object target, Class clazz, Method method, Object[] args) {
        Expression expression = getExpression(EXPRESSION_CACHE, new AnnotatedElementKey(method, target.getClass()), expressionStr);
        Object value = expression.getValue(createEvaluationContext(target, clazz, method, args));
        return value.toString();
    }

    public String evaluatorExpressionStr(@NonNull String expressionStr, Object target) {
        Expression expression = getExpression(EXPRESSION_CACHE, new AnnotatedElementKey(target.getClass(), target.getClass()), expressionStr);
        Object value = expression.getValue(createEvaluationContext(target));
        return value.toString();
    }


    private EvaluationContext createEvaluationContext(Object target, Class clazz, Method method, Object[] args) {
        return new MethodBasedEvaluationContext(
                getRootObject(target, clazz, method, args), method, args, getParameterNameDiscoverer());
    }

    private EvaluationContext createEvaluationContext(Object target) {
        return new StandardEvaluationContext(target);
    }


    protected Object getRootObject(Object target, Class clazz, Method method, Object[] args) {
        return new Object();
    }


}
