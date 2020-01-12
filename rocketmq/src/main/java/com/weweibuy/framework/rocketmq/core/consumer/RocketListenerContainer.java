package com.weweibuy.framework.rocketmq.core.consumer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * listener 容器
 *
 * @author durenhao
 * @date 2020/1/8 11:49
 **/
public interface RocketListenerContainer<T, R> {

    /**
     * 选项监听器
     *
     * @param list
     * @return
     */
    RocketMessageListener selectMessageListener(List<MessageExt> list);

    /**
     * 消费消息
     *
     * @param messageExtList
     * @param context        orderly  or concurrently
     * @return
     */
    R consume(List<MessageExt> messageExtList, T context);

    /**
     * 启动
     */
    void start() throws MQClientException;

    /**
     * 关闭
     */
    void shutdown();

    void setListeners(List<RocketMessageListener> listenerList);

}
