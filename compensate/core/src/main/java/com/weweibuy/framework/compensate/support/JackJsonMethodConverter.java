package com.weweibuy.framework.compensate.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weweibuy.framework.compensate.interfaces.model.CompensateInfo;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/2/14 22:35
 **/
public class JackJsonMethodConverter implements MethodArgsConverter {

    private final ObjectMapper objectMapper;

    private MethodArgsTypeHolder methodArgsTypeHolder;

    public JackJsonMethodConverter(ObjectMapper objectMapper, MethodArgsTypeHolder methodArgsTypeHolder) {
        this.objectMapper = objectMapper;
        this.methodArgsTypeHolder = methodArgsTypeHolder;
    }

    @Override
    public String convert(String compensateKey, Object[] args) {
        try {
            return objectMapper.writeValueAsString(args);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(args + "无法转为JSON");
        }
    }

    @Override
    public Object[] parser(CompensateInfo compensateInfo) {
        try {
            List list = objectMapper.readValue(compensateInfo.getMethodArgs(), List.class);
            if (list == null || list.size() == 0) {
                return null;
            }
            Object[] objects = new Object[list.size()];
            for (int i = 0; i < list.size(); i++) {
                String string = objectMapper.writeValueAsString(list.get(0));
                Object value = objectMapper.readValue(string, methodArgsTypeHolder.getType(generateKey(compensateInfo.getCompensateKey(), i)));
                objects[i] = value;
            }
            return objects;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(compensateInfo.getMethodArgs() + "无法转为MethodArgsWrapper 对象");
        }
    }


    private String generateKey(String compensateKey, Integer argsIndex) {
        return compensateKey + "_" + argsIndex;
    }
}
