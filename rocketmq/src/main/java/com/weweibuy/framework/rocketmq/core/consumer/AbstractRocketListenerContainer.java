package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.annotation.BatchHandlerModel;
import com.weweibuy.framework.rocketmq.core.MessageConverter;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 抽象监听容器
 *
 * @author durenhao
 * @date 2020/1/8 11:54
 **/
public abstract class AbstractRocketListenerContainer<T, R> implements RocketListenerContainer<T, R> {

    private MessageConverter messageConverter;

    private DefaultMQPushConsumer mqPushConsumer;

    private List<RocketMessageListener<R>> rocketMessageListenerList;

    private Map<String, RocketMessageListener<R>> listenerMap;

    private Integer batchSize;

    private BatchHandlerModel batchHandlerModel;

    public AbstractRocketListenerContainer(DefaultMQPushConsumer mqPushConsumer,
                                           Integer batchSize, BatchHandlerModel batchHandlerModel) {
        this.mqPushConsumer = mqPushConsumer;
        this.mqPushConsumer.setMessageListener(getMessageListener());
        this.batchSize = batchSize;
        this.batchHandlerModel = batchHandlerModel;
    }


    @Override
    public RocketMessageListener<R> selectMessageListener(List<MessageExt> list) {
        if (batchSize == 1) {
            if (listenerMap.containsKey("*")) {
                return rocketMessageListenerList.get(0);
            }
            return listenerMap.get(list.get(0).getTags());
        } else {
            return rocketMessageListenerList.get(0);
        }

    }

    @Override
    public RocketMessageListener<R> selectMessageListener(String tag) {
        return listenerMap.get(tag);
    }

    @Override
    public R consume(List<MessageExt> list, T context) {

        if (batchSize == 1) {
            // 单个消费
            MessageExt messageExt = list.get(0);
            String tags = messageExt.getTags();
            RocketMessageListener<R> rocketMessageListener = selectMessageListener(tags);
            return rocketMessageListener.onMessage(messageExt, context);
        } else if (batchHandlerModel.equals(BatchHandlerModel.FOREACH)) {
            // 批量迭代消费 TODO 返回值处理
            list.stream()
                    .forEach(m -> selectMessageListener(m.getTags()).onMessage(m));
            return (R) ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } else {
            // 批量一起消费
            return selectMessageListener(list)
                    .onMessage(list, context);
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
    public void setListeners(List<RocketMessageListener<R>> listenerList) {
        this.rocketMessageListenerList = listenerList;
        this.listenerMap = listenerList.stream()
                .collect(Collectors.toMap(l -> ((AbstractRocketMessageListener) l).getTag(), i -> i));
    }
}
