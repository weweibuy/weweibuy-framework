package com.weweibuy.framework.rocketmq.core;


import org.springframework.core.MethodParameter;

/**
 * @author durenhao
 * @date 2020/1/1 23:36
 **/
public class StringMessageConverter implements MessageConverter {

    @Override
    public byte[] toMessageBody(Object payload) {
        return payload.toString().getBytes();
    }

    @Override
    public Object fromMessageBody(byte[] payload, MethodParameter parameter) {
        return null;
    }

}
