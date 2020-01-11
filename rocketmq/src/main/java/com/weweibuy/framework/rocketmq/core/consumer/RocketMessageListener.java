package com.weweibuy.framework.rocketmq.core.consumer;

import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/1/8 11:14
 **/
public interface RocketMessageListener<R> {

    /**
     * 处理消息
     *
     * @param messageExtList
     * @return
     */
    R onMessage(List<MessageExt> messageExtList);


}
