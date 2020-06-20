package com.weweibuy.framework.idempotent.core.support;

import com.weweibuy.framework.common.core.expression.CommonCachedExpressionEvaluator;
import com.weweibuy.framework.idempotent.core.exception.IdempotentException;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

/**
 * 幂等key 解析器
 *
 * @author durenhao
 * @date 2020/6/19 22:37
 **/
public class IdempotentInfoParser extends CommonCachedExpressionEvaluator {

    private AnnotationMetaDataHolder annotationMetaDataHolder;

    private Map<String, IdempotentManager> idempotentManagerMap;

    private Map<String, KeyGenerator> keyGeneratorMap;

    private IdempotentManager idempotentManager;


    public IdempotentInfoParser(AnnotationMetaDataHolder annotationMetaDataHolder,
                                Map<String, IdempotentManager> idempotentManagerMap,
                                Map<String, KeyGenerator> keyGeneratorMap,
                                IdempotentManager idempotentManager) {
        Assert.notNull(annotationMetaDataHolder, "annotationMetaDataHolder 不能为空");
        Assert.notNull(idempotentManagerMap, "idempotentManagerMap 不能为空");
        Assert.notNull(idempotentManager, "idempotentManager 不能为空");
        this.annotationMetaDataHolder = annotationMetaDataHolder;
        this.idempotentManagerMap = idempotentManagerMap;
        this.keyGeneratorMap = keyGeneratorMap;
        this.idempotentManager = idempotentManager;
    }

    /**
     * 解析幂等key
     *
     * @param methodInvocation
     * @return
     */
    public IdempotentInfo parseIdempotentInfo(MethodInvocation methodInvocation) {
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

        IdempotentManager manager = idempotentManager;
        if (StringUtils.isNotBlank(metaData.getIdempotentManager())) {
            manager = getIdempotentManager(metaData.getIdempotentManager());
        }
        return new IdempotentInfo(key, sharding, metaData.getMaxLockMilli(), metaData.getReturnType(), manager);
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
        return Optional.ofNullable(keyGeneratorMap)
                .map(m -> m.get(name))
                .orElseThrow(() -> new IdempotentException("BeanName: " + name + " 对应的幂等Key生成器不存在"));
    }

    private IdempotentManager getIdempotentManager(String name) {
        return Optional.ofNullable(idempotentManagerMap.get(name))
                .orElseThrow(() -> new IdempotentException("BeanName: " + name + " 对应的幂等管理器不存在"));

    }

}
