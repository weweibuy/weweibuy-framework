package com.weweibuy.framework.rocketmq.core.consumer;

/**
 * @author durenhao
 * @date 2020/2/22 10:48
 **/
public interface MessageConsumerFilterChain {


    Object doFilter(Object messageObject, Object originContext);


}
