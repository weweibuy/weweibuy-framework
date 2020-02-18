package com.weweibuy.framework.rocketmq.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weweibuy.framework.rocketmq.core.MessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;

import java.io.IOException;

/**
 * @author durenhao
 * @date 2020/1/1 23:33
 **/
@Slf4j
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
        try {
            return objectMapper.readValue(payload, parameter.getParameterType());
        } catch (IOException e) {
            log.error("json 解析错误", e);
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Object fromMessageBody(byte[] payload, Class type) {
        try {
            return objectMapper.readValue(payload, type);
        } catch (IOException e) {
            log.error("json 解析错误", e);
            throw new IllegalArgumentException(e);
        }

    }

}
