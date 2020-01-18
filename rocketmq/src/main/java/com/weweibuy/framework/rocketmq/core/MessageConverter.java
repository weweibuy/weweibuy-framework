package com.weweibuy.framework.rocketmq.core;

import org.springframework.core.MethodParameter;

/**
 * RocketMQ 消息转化器
 *
 * @author durenhao
 * @date 2019/12/29 17:24
 **/
public interface MessageConverter {

    /**
     * 转化为 MQ 消息
     *
     * @param payload 消息体
     * @return
     */
    byte[] toMessageBody(Object payload);

    /**
     * 消息转对象
     *
     * @param payload
     * @return
     */
    Object fromMessageBody(byte[] payload, MethodParameter parameter);

    Object fromMessageBody(byte[] payload, Class type);



}
