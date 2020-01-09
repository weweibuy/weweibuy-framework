package com.weweibuy.framework.rocketmq.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;

/**
 * @author durenhao
 * @date 2020/1/1 23:33
 **/
public class JacksonRocketMqMessageConverter implements MessageConverter {

    private final ObjectMapper objectMapper;

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
    public Object fromMessageBody(byte[] payload, MethodParameter parameter) {
        return null;
    }

}
