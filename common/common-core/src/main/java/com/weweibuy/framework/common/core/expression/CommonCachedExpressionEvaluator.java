package com.weweibuy.framework.common.core.expression;

import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
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

    private static final Map<ExpressionKey, Expression> bizIdExpressionCache = new ConcurrentHashMap<>();


    public String evaluatorExpressionStr(@NonNull String expressionStr, Object target, Class clazz, Method method, Object[] args) {
        Expression expression = getExpression(bizIdExpressionCache, new AnnotatedElementKey(method, target.getClass()), expressionStr);
        Object value = expression.getValue(createEvaluationContext(target, clazz, method, args));
        return value.toString();
    }


    private EvaluationContext createEvaluationContext(Object target, Class clazz, Method method, Object[] args) {
        return new MethodBasedEvaluationContext(
                getRootObject(target, clazz, method, args), method, args, getParameterNameDiscoverer());
    }

    protected abstract Object getRootObject(Object target, Class clazz, Method method, Object[] args);


}
