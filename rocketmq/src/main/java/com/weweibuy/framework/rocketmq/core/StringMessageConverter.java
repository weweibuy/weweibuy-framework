package com.weweibuy.framework.rocketmq.core;

/**
 * @author durenhao
 * @date 2020/1/1 23:36
 **/
public class StringMessageConverter implements MessageConverter {

    @Override
    public byte[] toMessageBody(Object payload) {
        return payload.toString().getBytes();
    }
}
