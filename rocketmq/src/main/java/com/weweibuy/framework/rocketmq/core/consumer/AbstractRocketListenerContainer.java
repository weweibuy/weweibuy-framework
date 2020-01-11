package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.core.MessageConverter;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.exception.MQClientException;

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

    public AbstractRocketListenerContainer(DefaultMQPushConsumer mqPushConsumer) {
        this.mqPushConsumer = mqPushConsumer;
        this.mqPushConsumer.setMessageListener(getMessageListener());
    }

    @Override
    public RocketMessageListener selectMessageListener(String tag, Map map) {
        return null;
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
}
