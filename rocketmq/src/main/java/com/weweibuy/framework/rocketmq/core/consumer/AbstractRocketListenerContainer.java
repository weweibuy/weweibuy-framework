package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.core.MessageConverter;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Map;

/**
 * 抽象监听容器
 *
 * @author durenhao
 * @date 2020/1/8 11:54
 **/
public abstract class AbstractRocketListenerContainer<T, R> implements RocketListenerContainer<T, R> {

    private MessageConverter messageConverter;

    private DefaultMQPushConsumer mqPushConsumer;

    private List<RocketMessageListener> rocketMessageListenerList;

    private Map<String, RocketMessageListener> listenerMap;

    private Integer batchSize;

    public AbstractRocketListenerContainer(DefaultMQPushConsumer mqPushConsumer,
                                           Integer batchSize) {
        this.mqPushConsumer = mqPushConsumer;
        this.mqPushConsumer.setMessageListener(getMessageListener());
        this.batchSize = batchSize;
    }


    @Override
    public RocketMessageListener selectMessageListener(List<MessageExt> list) {
        if (batchSize == 1) {
            if (listenerMap.containsKey("*")) {
                return rocketMessageListenerList.get(0);
            }
            return listenerMap.get(list.get(0).getTags());
        } else {
            return rocketMessageListenerList.get(0);
        }

    }

    /**
     * 获取MQ 自身的MessageListener
     *
     * @return
     */
    protected abstract MessageListener getMessageListener();


    @Override
    public void start() throws MQClientException {
        mqPushConsumer.start();
    }

    @Override
    public void shutdown() {
        mqPushConsumer.shutdown();
    }

    @Override
    public void setListeners(List<RocketMessageListener> listenerList) {
        this.rocketMessageListenerList = listenerList;
    }
}
