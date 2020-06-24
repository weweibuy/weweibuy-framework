package com.weweibuy.framework.rocketmq.support.producer;

import com.weweibuy.framework.common.core.support.ObjectWrapper;
import com.weweibuy.framework.rocketmq.core.producer.MessageSendContext;
import com.weweibuy.framework.rocketmq.core.producer.MessageSendFilter;
import com.weweibuy.framework.rocketmq.core.producer.MessageSendFilterChain;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.Collection;

/**
 * 消息发送日志过滤器
 *
 * @author durenhao
 * @date 2020/2/20 20:32
 **/
@Slf4j
public class LogMessageSendFilter implements MessageSendFilter {


    @Override
    public Object filter(MessageSendContext context, Object message, MessageSendFilterChain chain) throws Throwable {
        Object result = null;
        if (context.isBatch()) {
            Collection<Message> messageCollection = (Collection<Message>) message;
            try {
                result = chain.doFilter(context, message);
            } finally {
                ObjectWrapper<Object> objectWrapper = new ObjectWrapper<>(result);
                messageCollection.forEach(m -> doLog(context, m, objectWrapper.getObject()));
            }
        } else {
            Message mg = (Message) message;
            try {
                result = chain.doFilter(context, message);
            } finally {
                doLog(context, mg, result);
            }
        }
        return result;
    }


    private void doLog(MessageSendContext context, Message message, Object sendResult) {
        log.info("MQ消息 Topic:【{}】, Tag:【{}】, Key:【{}】, Body: {}, 发送结果: {}",
                message.getTopic(),
                message.getTags(),
                message.getKeys(),
                new String(message.getBody()),
                sendResult(context, sendResult));
    }

    private String sendResult(MessageSendContext context, Object sendResult) {

        if (sendResult == null) {
            if (MessageSendContext.SendModel.shouldHasResult(context.getSendModel())) {
                return "已发送";
            } else {
                return "null";
            }
        }
        if (sendResult instanceof SendResult) {
            return ((SendResult) sendResult).getSendStatus().toString();
        }
        return StringUtils.EMPTY;

    }


    @Override
    public Integer getOrder() {
        return 0;
    }
}
