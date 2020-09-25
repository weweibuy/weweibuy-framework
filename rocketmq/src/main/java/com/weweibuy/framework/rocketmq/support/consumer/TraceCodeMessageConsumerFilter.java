package com.weweibuy.framework.rocketmq.support.consumer;

import com.weweibuy.framework.common.core.utils.IdWorker;
import com.weweibuy.framework.common.log.support.LogTraceContext;
import com.weweibuy.framework.rocketmq.core.consumer.ConsumerFilter;
import com.weweibuy.framework.rocketmq.core.consumer.MessageConsumerFilterChain;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/9/25 22:51
 **/
public class TraceCodeMessageConsumerFilter implements ConsumerFilter {

    @Override
    public Object filter(List<MessageExt> messageExtList, Object originContext, MessageConsumerFilterChain chain) {
        LogTraceContext.setTraceCode(IdWorker.nextStringId());
        try {
            return chain.doFilter(messageExtList, originContext);
        } finally {
            LogTraceContext.clear();
        }
    }

    @Override
    public int getOrder() {
        return -500;
    }
}
