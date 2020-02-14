package com.weweibuy.framework.compensate.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author durenhao
 * @date 2020/2/14 22:35
 **/
public class JackJsonMethodArgsWrapperConverter implements MethodArgsWrapperConverter {

    private final ObjectMapper objectMapper;

    public JackJsonMethodArgsWrapperConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convert(MethodArgsWrapper methodArgs) {
        try {
            return objectMapper.writeValueAsString(methodArgs);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(methodArgs + "无法转为JSON");
        }
    }

    @Override
    public MethodArgsWrapper parser(String str) {
        try {
            return objectMapper.readValue(str, MethodArgsWrapper.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(str + "无法转为MethodArgsWrapper 对象");
        }
    }
}
