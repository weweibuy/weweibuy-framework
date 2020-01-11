package com.weweibuy.framework.rocketmq.core.consumer;

import java.util.List;

/**
 * 监听容器创建工厂
 *
 * @author durenhao
 * @date 2020/1/11 21:58
 **/
public interface RocketListenerContainerFactory {

    /**
     * 创建监听容器
     *
     * @param endpointList
     * @return
     */
    RocketListenerContainer createListenerContainer(List<MethodRocketListenerEndpoint> endpointList);


}
