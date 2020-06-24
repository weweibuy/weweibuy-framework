package com.weweibuy.framework.rocketmq.core.producer;

import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.Collection;
import java.util.List;

/**
 * 非线程安全
 *
 * @author durenhao
 * @date 2020/2/20 20:27
 **/
public class MessageSendFilterEnter implements MessageSendFilterChain {

    private int pos = 0;

    private int size;

    private List<MessageSendFilter> messageSendFilterList;

    private final MQProducer mqProducer;

    public MessageSendFilterEnter(List<MessageSendFilter> messageSendFilterList, MQProducer mqProducer) {
        this.messageSendFilterList = messageSendFilterList;
        this.size = messageSendFilterList.size();
        this.mqProducer = mqProducer;
    }

    @Override
    public Object doFilter(MessageSendContext context, Object message) throws Throwable {
        if (pos < size) {
           return messageSendFilterList.get(pos++).filter(context, message,this);
        }
        return send(context, message, mqProducer);

    }


    private Object send(MessageSendContext context, Object message, MQProducer mqProducer) throws Exception {
        MessageSendContext.SendModel sendModel = context.getSendModel();

        switch (sendModel) {
            case BATCH:
                return mqProducer.send((Collection) message, context.getTimeout());
            case ONE_WAY_ORDERLY:
                mqProducer.sendOneway((Message) message, context.getMessageQueueSelector(), ((Message) message).getKeys());
                return null;
            case ONE_WAY_NOT_ORDERLY:
                mqProducer.sendOneway((Message) message);
                return null;
            case ORDERLY_ASYNC:
                mqProducer.send((Message) message, context.getMessageQueueSelector(), ((Message) message).getKeys(), context.getSendCallback(), context.getTimeout());
                return null;
            case ORDERLY_NOT_ASYNC:
                return mqProducer.send((Message) message, context.getMessageQueueSelector(), ((Message) message).getKeys(), context.getTimeout());

            case NORMAL_ASYNC:
                mqProducer.send((Message) message, context.getSendCallback(), context.getTimeout());
                return null;
            case NORMAL:
                return mqProducer.send((Message) message, context.getTimeout());
            default:
                return null;

        }
    }
}
