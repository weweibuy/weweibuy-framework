package com.weweibuy.framework.idempotent.core.support;

import com.fasterxml.jackson.databind.JavaType;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import com.weweibuy.framework.idempotent.core.annotation.Idempotent;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 注解上元数据持有者
 *
 * @author durenhao
 * @date 2020/6/19 23:41
 **/
public class AnnotationMetaDataHolder {

    private Map<Method, IdempotentAnnotationMeta> hashMap = new HashMap();

    public void putMetaData(Method method, Idempotent idempotent) {
        MethodParameter methodParameter = new MethodParameter(method, -1);
        Type parameterType = methodParameter.getNestedGenericParameterType();
        JavaType javaType = JackJsonUtils.getCamelCaseMapper().getTypeFactory().constructType(GenericTypeResolver.resolveType(parameterType, method.getReturnType()));
        hashMap.put(method, new IdempotentAnnotationMeta(idempotent, javaType));
    }

    public IdempotentAnnotationMeta getMetaData(Method method) {
        return hashMap.get(method);
    }

}
