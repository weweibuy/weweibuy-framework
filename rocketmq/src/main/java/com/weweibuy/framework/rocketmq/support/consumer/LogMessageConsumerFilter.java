package com.weweibuy.framework.rocketmq.support.consumer;

import com.weweibuy.framework.rocketmq.core.consumer.ConsumerFilter;
import com.weweibuy.framework.rocketmq.core.consumer.MessageConsumerFilterChain;
import com.weweibuy.framework.rocketmq.support.RocketMqLogger;
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
    public Object filter(List<MessageExt> messageExtList, Object originContext, MessageConsumerFilterChain chain) {
        Object result = null;
        long timeMillis = System.currentTimeMillis();
        logReceive(messageExtList, originContext);
        try {
            result = chain.doFilter(messageExtList, originContext);
        } finally {
            timeMillis = System.currentTimeMillis() - timeMillis;
            log(messageExtList, originContext, result, timeMillis);
        }
        return result;

    }


    private void logReceive(List<MessageExt> messageExtList, Object originContext) {
        if (messageExtList.size() == 1) {
            doLogReceive(messageExtList.get(0), originContext);
        } else {
            messageExtList.stream()
                    .forEach(m -> doLogReceive(m, originContext));
        }

    }

    private void log(List<MessageExt> messageExtList, Object originContext, Object result, long timeMillis) {
        if (messageExtList.size() == 1) {
            doLog(messageExtList.get(0), originContext, result, timeMillis);
        } else {
            messageExtList.stream()
                    .forEach(m -> doLog(m, originContext, result, timeMillis));
        }

    }

    private void doLogReceive(MessageExt messageExt, Object originContext) {
        RocketMqLogger.logReceiveMessage(messageExt, originContext);
    }

    private void doLog(MessageExt messageExt, Object originContext, Object result, long timeMillis) {
        RocketMqLogger.logConsumerMessage(messageExt, originContext, result, timeMillis);
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
