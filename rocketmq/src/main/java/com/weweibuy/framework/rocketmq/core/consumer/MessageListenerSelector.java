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
