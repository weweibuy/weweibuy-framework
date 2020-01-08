/*
 * All rights Reserved, Designed By baowei
 *
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.weweibuy.framework.rocketmq.core.consumer;

import java.util.Map;

/**
 * @author durenhao
 * @date 2020/1/8 11:28
 **/
public class MessageListenerSelector {

    public RocketMessageListener selectMessageListener(String tag, Map<String, RocketMessageListener> listenerMap) {
        return listenerMap.get(tag);
    }


}
