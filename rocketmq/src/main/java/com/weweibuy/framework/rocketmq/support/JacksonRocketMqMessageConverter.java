package com.weweibuy.framework.rocketmq.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weweibuy.framework.rocketmq.core.MessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author durenhao
 * @date 2020/1/1 23:33
 **/
@Slf4j
public class JacksonRocketMqMessageConverter implements MessageConverter {

    private final ObjectMapper objectMapper;

    private final Map<MethodParameter, JavaType> javaTypeMap = new ConcurrentHashMap<>(64);

    public JacksonRocketMqMessageConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] toMessageBody(Object payload) {
        try {
            return objectMapper.writeValueAsBytes(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(payload.toString() + " 无法转为Json");
        }
    }

    @Override
    public Object fromMessageBody(byte[] payload, MethodParameter parameter, boolean batch) {
        try {
            return objectMapper.readValue(payload, javaType(parameter, batch));
        } catch (IOException e) {
            log.error("json 解析错误", e);
            throw new IllegalArgumentException(e);
        }
    }

    private JavaType javaType(MethodParameter parameter, boolean batch) {

        if (!batch) {
            return javaTypeMap.computeIfAbsent(parameter, p -> {
                Type nestedGenericParameterType = p.getNestedGenericParameterType();
                Type type = GenericTypeResolver.resolveType(p.getNestedGenericParameterType(), p.getParameterType());

                return objectMapper.getTypeFactory().constructType(type);
            });
        }

        return javaTypeMap.computeIfAbsent(parameter, p -> {
            Type parameterType = p.getNestedGenericParameterType();

            Type arg = null;
            if (parameterType instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) parameterType).getActualTypeArguments();
                arg = args[0];
            }
            return objectMapper.getTypeFactory().constructType(arg);
        });
    }


}
