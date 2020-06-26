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
        try {
            result = chain.doFilter(messageExtList, originContext);
        } finally {
            log(messageExtList, originContext, result);
        }
        return result;

    }

    private void log(List<MessageExt> messageExtList, Object originContext, Object result) {
        if (messageExtList.size() == 1) {
            doLog(messageExtList.get(0), originContext, result);
        } else {
            messageExtList.stream()
                    .forEach(m -> doLog(m, originContext, result));
        }

    }

    private void doLog(MessageExt messageExt, Object originContext, Object result) {
        RocketMqLogger.logConsumerMessage(messageExt, originContext, result);
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
