package com.weweibuy.framework.rocketmq.core.provider;

import lombok.Data;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;

import java.util.HashSet;
import java.util.Set;

/**
 * @author durenhao
 * @date 2020/2/20 20:24
 **/
@Data
public class MessageSendContext {

    private SendModel sendModel;

    private MessageQueueSelector messageQueueSelector;

    private SendCallback sendCallback;

    private Integer timeout;

    private String topic;

    private String tag;


    public MessageSendContext(RocketMethodMetadata metadata, MessageQueueSelector messageQueueSelector, Object[] args) {
        this.topic = metadata.getTopic();
        this.tag = metadata.getTag();
        this.timeout = metadata.getTimeout();
        this.messageQueueSelector = messageQueueSelector;
        this.sendCallback = sendCallback;


        if (metadata.getBatch()) {
            sendModel = SendModel.BATCH;
        } else if (metadata.getOneWay() && metadata.getOrderly()) {
            sendModel = SendModel.ONE_WAY_ORDERLY;
        } else if (metadata.getOneWay() && !metadata.getOrderly()) {
            sendModel = SendModel.ONE_WAY_NOT_ORDERLY;
        } else if (metadata.getOrderly() && metadata.getAsyncIndex() != null) {
            this.sendCallback = (SendCallback) args[metadata.getAsyncIndex()];
            sendModel = SendModel.ORDERLY_ASYNC;
        } else if (metadata.getOrderly() && metadata.getAsyncIndex() == null) {
            sendModel = SendModel.ORDERLY_NOT_ASYNC;
        } else if (metadata.getAsyncIndex() != null) {
            this.sendCallback = (SendCallback) args[metadata.getAsyncIndex()];
            sendModel = SendModel.NORMAL_ASYNC;
        } else {
            sendModel = SendModel.NORMAL;
        }

    }


    public boolean isBatch() {
        return SendModel.BATCH.equals(sendModel);
    }


    public enum SendModel {

        BATCH,

        ONE_WAY_ORDERLY,

        ONE_WAY_NOT_ORDERLY,

        ORDERLY_ASYNC,

        ORDERLY_NOT_ASYNC,

        NORMAL_ASYNC,

        NORMAL,
        ;

        private static Set<SendModel> noResultSet = new HashSet<>(4);

        static {
            noResultSet.add(ONE_WAY_ORDERLY);
            noResultSet.add(ONE_WAY_NOT_ORDERLY);
            noResultSet.add(ORDERLY_ASYNC);
            noResultSet.add(NORMAL_ASYNC);
        }

        public static boolean shouldHasResult(SendModel sendModel) {
            return noResultSet.contains(sendModel);
        }

    }

}
