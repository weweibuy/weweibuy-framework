package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.core.producer.MessageSendContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 日志输出
 *
 * @author durenhao
 * @date 2020/6/26 12:47
 **/
@Slf4j
public class RocketMqLogger {

    /**
     * 消费异常日志
     *
     * @param list
     * @param originContext
     * @param exception
     */
    public static void logConsumerException(List<MessageExt> list, Object originContext, Exception exception) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RocketMQ 消费MQ消息: ");
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(" Topic:【").append(list.get(i).getTags()).append("】")
                    .append(" Tag:【").append(list.get(i).getTags()).append("】")
                    .append(" Key:【").append(list.get(i).getKeys()).append("】")
                    .append(" Body:【").append(new String(list.get(i).getBody())).append("】");
            if (i + 1 != list.size()) {
                stringBuilder.append("\r\n");
            }
        }
        stringBuilder.append("出现异常: ");
        log.warn(stringBuilder.toString(), exception);
    }

    /**
     * 发送日志
     *
     * @param context
     * @param message
     * @param sendResult
     */
    public static void logProducerMessage(MessageSendContext context, Message message, Object sendResult) {
        log.info("RocketMQ 发送消息: Topic:【{}】, Tag:【{}】, Key:【{}】, Body: {}, 发送结果: {}",
                message.getTopic(),
                message.getTags(),
                message.getKeys(),
                new String(message.getBody()),
                sendResult(context, sendResult));
    }

    private static String sendResult(MessageSendContext context, Object sendResult) {
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

    /**
     * 输出消费日志
     *
     * @param messageExt
     * @param originContext
     * @param result
     */
    public static void logConsumerMessage(MessageExt messageExt, Object originContext, Object result, long elapsedTime) {
        log.info("RocketMQ 消费消息: 消费结果: {}, 耗时: {}",
                result, elapsedTime);
    }


    /**
     * 收到MQ消息
     *
     * @param messageExt
     * @param originContext
     */
    public static void logReceiveMessage(MessageExt messageExt, Object originContext) {
        log.info("RocketMQ 收到消息: Topic:【{}】, Tag:【{}】, Key:【{}】, Body: {}",
                messageExt.getTopic(),
                messageExt.getTags(),
                messageExt.getKeys(),
                new String(messageExt.getBody()));
    }
}
