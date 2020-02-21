package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.annotation.BatchHandlerModel;
import com.weweibuy.framework.rocketmq.core.MessageConverter;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
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

    private R success;

    private R fail;

    public AbstractRocketListenerContainer(DefaultMQPushConsumer mqPushConsumer,
                                           Integer batchSize, BatchHandlerModel batchHandlerModel, R success, R fail) {
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
            // TODO 批量消息的形式 无法支持Tag 选择
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
            boolean match = list.stream()
                    .map(m -> selectMessageListener(m.getTags()).onMessage(m, context))
                    .anyMatch(r -> !isSuccess(r));
            if (match) {
                return fail;
            }
            return success;
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

    protected abstract boolean isSuccess(R r);

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
