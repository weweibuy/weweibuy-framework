package com.weweibuy.framework.rocketmq.support.provider;

import com.weweibuy.framework.rocketmq.core.provider.MessageSendContext;
import com.weweibuy.framework.rocketmq.core.provider.MessageSendFilter;
import com.weweibuy.framework.rocketmq.core.provider.MessageSendFilterChain;
import lombok.extern.slf4j.Slf4j;
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
        Object result;
        if (context.isBatch()) {
            Collection<Message> messageCollection = (Collection<Message>) message;
            messageCollection.forEach(this::doLog);
            result = chain.doFilter(context, message);
            messageCollection.forEach(m -> doLog(context, m, result));
        } else {
            Message mg = (Message) message;
            doLog(mg);
            result = chain.doFilter(context, message);
            doLog(context, mg, result);
        }
        return result;
    }


    private void doLog(Message message) {
        log.info("发送MQ消息 Topic:【{}】, Tag:【{}】, Key:【{}】, 消息体: {}", message.getTopic(), message.getTags(), message.getKeys(), new String(message.getBody()));
    }

    private void doLog(MessageSendContext context, Message message, Object sendResult) {
        if (sendResult == null) {
            log.info("MQ消息 Topic:【{}】, Tag:【{}】, Key:【{}】, 发送模式: 【{}】, 已经发送",
                    message.getTopic(), message.getTags(), message.getKeys(), context.getSendModel());
        } else if (sendResult instanceof SendResult) {
            log.info("MQ消息 Topic:【{}】, Tag:【{}】, Key:【{}】, 发送结果: {}",
                    message.getTopic(), message.getTags(), message.getKeys(), ((SendResult) sendResult).getSendStatus());
        }
    }

    @Override
    public Integer getOrder() {
        return 0;
    }
}
