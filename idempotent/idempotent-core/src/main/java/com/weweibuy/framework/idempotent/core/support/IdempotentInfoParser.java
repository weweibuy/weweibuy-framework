package com.weweibuy.framework.idempotent.core.support;

import com.weweibuy.framework.common.core.expression.CommonCachedExpressionEvaluator;
import com.weweibuy.framework.idempotent.core.aspect.AnnotationMetaDataHolder;
import com.weweibuy.framework.idempotent.core.aspect.IdempotentAnnotationMeta;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

/**
 * 幂等key 解析器
 *
 * @author durenhao
 * @date 2020/6/19 22:37
 **/
public class IdempotentInfoParser extends CommonCachedExpressionEvaluator {

    private AnnotationMetaDataHolder annotationMetaDataHolder;

    private ApplicationContext applicationContext;

    /**
     * 解析幂等key
     *
     * @param methodInvocation
     * @return
     */
    public IdempotentInfo parseKey(MethodInvocation methodInvocation) {
        Method method = methodInvocation.getMethod();
        Object target = methodInvocation.getThis();
        Object[] arguments = methodInvocation.getArguments();
        // TODO 获取真实方法 而非代理方法?
        IdempotentAnnotationMeta metaData = annotationMetaDataHolder.getMetaData(method);

        String key = evaluatorKey(metaData.getKey(), target, method, arguments);
        KeyGenerator keyGenerator = null;
        if (StringUtils.isNotBlank(metaData.getGenerator()) && (keyGenerator = getKeyGenerator(metaData.getGenerator())) != null) {
            key = keyGenerator.generatorKey(key);
        }
        String sharding = evaluatorKey(metaData.getSharding(), target, method, arguments);
        return new IdempotentInfo(key, sharding, metaData.getMaxLockMilli());
    }

    private String evaluatorKey(String keyInAnnotation, Object target, Method method, Object[] args) {
        Class<?> clazz = target.getClass();
        if (StringUtils.isBlank(keyInAnnotation)) {
            return clazz.getName() + "_" + method.getName();
        }
        return evaluatorExpressionStr(keyInAnnotation, target, clazz, method, args);
    }


    @Override
    protected Object getRootObject(Object target, Class clazz, Method method, Object[] args) {
        return new IdempotentExpressionRootObject(args, method, target, clazz);
    }


    private KeyGenerator getKeyGenerator(String name) {
        return applicationContext.getBean(name, KeyGenerator.class);
    }

}
