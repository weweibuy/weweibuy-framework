package com.weweibuy.framework.rocketmq.core;

import org.apache.rocketmq.common.message.Message;

import java.util.Map;

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
     * @param headers 消息属性
     * @return
     * @see org.apache.rocketmq.common.message.Message#properties
     */
    Message toMessage(Object payload, Map<String, String> headers);


}
