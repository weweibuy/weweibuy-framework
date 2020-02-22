package com.weweibuy.framework.rocketmq.support.consumer;

import com.weweibuy.framework.rocketmq.core.consumer.ConsumerFilter;
import com.weweibuy.framework.rocketmq.core.consumer.MessageConsumerFilterChain;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/2/22 11:10
 **/
@Slf4j
public class LogMessageConsumerFilter implements ConsumerFilter {


    @Override
    public Object filter(Object messageObject, Object originContext, MessageConsumerFilterChain chain) {
        if (messageObject instanceof MessageExt) {
            doLog((MessageExt) messageObject);
        } else if (messageObject instanceof List) {
            List messageObjectList = (List) messageObject;
            messageObjectList.stream()
                    .filter(m -> m instanceof MessageExt)
                    .forEach(m -> doLog((MessageExt) m));
        } else {
            log.info("收到MQ 消息: {}", messageObject);
        }

        Object result = chain.doFilter(messageObject, originContext);
        doLogResult(result);
        return result;

    }

    private void doLog(MessageExt messageExt) {
        log.info("消费MQ消息: Topic:【{}】, Tag:【{}】, Body: {} ",
                messageExt.getTopic(), messageExt.getTags(), new String(messageExt.getBody()));
    }

    private void doLogResult(Object result) {
        log.info("消费MQ消息: 结果: {}", result);
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
