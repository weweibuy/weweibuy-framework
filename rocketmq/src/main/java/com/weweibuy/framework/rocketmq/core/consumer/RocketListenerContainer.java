/*
 * All rights Reserved, Designed By baowei
 *
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.weweibuy.framework.rocketmq.core.consumer;

import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Map;

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
     * @param tag
     * @param listenerMap
     * @return
     */
    RocketMessageListener selectMessageListener(String tag, Map<String, RocketMessageListener> listenerMap);

    /**
     * 消费消息
     *
     * @param messageExtList
     * @param context        orderly  or concurrently
     * @return
     */
    R consume(List<MessageExt> messageExtList, T context);

}
