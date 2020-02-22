package com.weweibuy.framework.rocketmq.core.consumer;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/2/22 10:49
 **/
public class MessageConsumerFilterEnter implements MessageConsumerFilterChain {

    private int pos = 0;

    private int size;

    private List<ConsumerFilter> messageSendFilterList;

    private RocketMessageListener listener;


    public MessageConsumerFilterEnter(List<ConsumerFilter> messageSendFilterList, RocketMessageListener listener) {
        this.messageSendFilterList = messageSendFilterList;
        this.listener = listener;
        this.size = messageSendFilterList.size();
    }

    @Override
    public Object doFilter(Object messageObject, Object originContext)  {
        if (pos < size) {
            return messageSendFilterList.get(pos++).filter(messageObject, originContext, this);
        }
        return onMessage(messageObject, originContext);
    }


    public Object onMessage(Object messageObject, Object originContext)  {
        return listener.onMessage(messageObject, originContext);
    }
}
