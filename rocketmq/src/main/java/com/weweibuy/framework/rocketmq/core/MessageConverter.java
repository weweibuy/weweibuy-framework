package com.weweibuy.framework.rocketmq.core;

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


}
