package com.weweibuy.framework.compensate.support;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author durenhao
 * @date 2020/2/15 12:07
 **/
public class MethodArgsTypeHolder {

    private Map<String, JavaType> typeMap = new HashMap<>();

    private final ObjectMapper objectMapper;

    public MethodArgsTypeHolder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JavaType getType(String key) {
        return typeMap.get(key);
    }

    public synchronized void addType(String key, MethodParameter methodParameter, Class type) {
        Type parameterType = methodParameter.getNestedGenericParameterType();
        JavaType javaType = objectMapper.getTypeFactory().constructType(GenericTypeResolver.resolveType(parameterType, type));
        typeMap.put(key, javaType);
    }

}
