package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.annotation.BatchHandlerModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 抽象监听容器
 *
 * @author durenhao
 * @date 2020/1/8 11:54
 **/
@Slf4j
public abstract class AbstractRocketListenerContainer<T, R> implements RocketListenerContainer<T, R> {

    private DefaultMQPushConsumer mqPushConsumer;

    private List<RocketMessageListener<R>> rocketMessageListenerList;

    private Map<String, RocketMessageListener<R>> listenerMap;

    private List<ConsumerFilter> messageSendFilterList;

    private Integer batchSize;

    private BatchHandlerModel batchHandlerModel;

    private R success;

    private R fail;

    public AbstractRocketListenerContainer(DefaultMQPushConsumer mqPushConsumer,
                                           Integer batchSize, BatchHandlerModel batchHandlerModel,
                                           List<ConsumerFilter> messageSendFilterList, R success, R fail) {
        this.mqPushConsumer = mqPushConsumer;
        this.mqPushConsumer.setMessageListener(getMessageListener());
        this.batchSize = batchSize;
        this.batchHandlerModel = batchHandlerModel;
        if (CollectionUtils.isEmpty(messageSendFilterList)) {
            messageSendFilterList = Collections.emptyList();
        }
        this.messageSendFilterList = messageSendFilterList.stream()
                .sorted(Comparator.comparing(ConsumerFilter::getOrder)).collect(Collectors.toList());
        this.fail = fail;
        this.success = success;
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
        if (org.apache.commons.lang3.StringUtils.isBlank(tag)) {
            // tag 为空 说明监听tag为*, tag为* 监听有且只能有一个
            return listenerMap.get("*");
        }
        return listenerMap.get(tag);
    }

    @Override
    public R consume(List<MessageExt> list, T context) {
        return filterAndOnMessage(list, context);
    }

    private R filterAndOnMessage(List<MessageExt> list, T originContext) {
        return (R) new MessageConsumerFilterEnter()
                .doFilter(list, originContext);
    }


    class MessageConsumerFilterEnter<T> implements MessageConsumerFilterChain {

        private int pos = 0;

        private int size = messageSendFilterList.size();

        @Override
        public Object doFilter(List<MessageExt> messageExtList, Object originContext) {
            if (pos < size) {
                return messageSendFilterList.get(pos++).filter(messageExtList, originContext, this);
            }
            return onMessage(messageExtList, originContext);
        }


        public Object onMessage(List<MessageExt> messageExtList, Object originContext) {
            if (batchSize == 1) {
                // 单个消费
                MessageExt messageExt = messageExtList.get(0);
                String tags = messageExt.getTags();
                RocketMessageListener<R> rocketMessageListener = selectMessageListener(tags);
                return checkListenerAndOnMessage(rocketMessageListener, messageExt, originContext);
            } else if (batchHandlerModel.equals(BatchHandlerModel.FOREACH)) {
                boolean match = messageExtList.stream()
                        .map(m -> checkListenerAndOnMessage(selectMessageListener(m.getTags()), m, originContext))
                        .anyMatch(r -> !isSuccess(r));
                if (match) {
                    return fail;
                }
                return success;
            } else {
                // 批量一起消费
                return checkListenerAndOnMessage(selectMessageListener(messageExtList), messageExtList, originContext);
            }
        }
    }

    private R checkListenerAndOnMessage(RocketMessageListener<R> messageListener, Object messageObject, Object originContext) {
        if (messageListener == null) {
            log.error("MQ消息: {},  无法找到对应的监听器", messageObject);
            throw new IllegalStateException("MQ消息 : [" + messageObject + "], 无法找到对应的监听器");
        }
        return messageListener.onMessage(messageObject, originContext);
    }


    /**
     * 获取MQ 自身的MessageListener
     *
     * @return
     */
    protected abstract MessageListener getMessageListener();

    protected boolean isSuccess(R r) {
        return success.equals(r);
    }

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

        Map<String, RocketMessageListener<R>> rocketMessageListenerMap = new HashMap<>();

        for (RocketMessageListener listener : listenerList) {
            AbstractRocketMessageListener rocketMessageListener = (AbstractRocketMessageListener) listener;
            String[] split = rocketMessageListener.getTag().split("\\|\\|");
            Assert.notNull(split, "RocketMessageListener 的 Tag不能为空");
            if (split.length == 1) {
                rocketMessageListenerMap.put(split[0], listener);
            } else {
                Arrays.stream(split).forEach(t -> rocketMessageListenerMap.put(t, listener));
            }

        }

        this.listenerMap = rocketMessageListenerMap;
    }
}
