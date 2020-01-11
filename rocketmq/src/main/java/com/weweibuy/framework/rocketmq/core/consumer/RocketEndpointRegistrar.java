package com.weweibuy.framework.rocketmq.core.consumer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.client.exception.MQClientException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2020/1/9 12:13
 **/
public class RocketEndpointRegistrar {

    private List<MethodRocketListenerEndpoint> endpointList;

    private List<RocketListenerContainer> listenerContainerList;

    private final RocketListenerContainerFactory containerFactory;

    public RocketEndpointRegistrar(RocketListenerContainerFactory containerFactory) {
        this.containerFactory = containerFactory;
    }

    /**
     * 注册监听容器
     *
     * @return
     */
    public void registerListenerContainer() {
        Map<String, List<MethodRocketListenerEndpoint>> collect = endpointList.stream()
                .collect(Collectors.groupingBy(e -> e.getTopic() + "_" + e.getGroup()));
        listenerContainerList = collect.entrySet().stream()
                .map(e -> containerFactory.createListenerContainer(e.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * 启动全部容器
     */
    public synchronized void startUpAllContainer() {
        if (CollectionUtils.isNotEmpty(listenerContainerList)) {
            listenerContainerList.forEach(c -> {
                try {
                    c.start();
                } catch (MQClientException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    /**
     * 关闭全部容器
     */
    public synchronized void shutdownAllContainer() {
        if (CollectionUtils.isNotEmpty(listenerContainerList)) {
            listenerContainerList.forEach(c -> c.shutdown());
        }
    }


    public void setEndpointList(List<MethodRocketListenerEndpoint> endpointList) {
        this.endpointList = endpointList;
    }


}
