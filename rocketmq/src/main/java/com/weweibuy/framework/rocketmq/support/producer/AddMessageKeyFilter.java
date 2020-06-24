package com.weweibuy.framework.rocketmq.support.producer;

import com.weweibuy.framework.common.core.utils.IdWorker;
import com.weweibuy.framework.rocketmq.core.producer.MessageSendContext;
import com.weweibuy.framework.rocketmq.core.producer.MessageSendFilter;
import com.weweibuy.framework.rocketmq.core.producer.MessageSendFilterChain;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.Message;

import java.util.Collection;

/**
 * 增加消息 key 过滤器
 *
 * @author durenhao
 * @date 2020/6/21 20:21
 **/
public class AddMessageKeyFilter implements MessageSendFilter {

    @Override
    public Object filter(MessageSendContext context, Object message, MessageSendFilterChain chain) throws Throwable {
        if (message instanceof Message) {
            setKeyIfNecessary(message);
        } else if (message instanceof Collection) {
            Collection msgList = (Collection) message;
            msgList.forEach(this::setKeyIfNecessary);
        }
        return chain.doFilter(context, message);
    }

    @Override
    public Integer getOrder() {
        return -500;
    }

    private void setKeyIfNecessary(Object message) {
        if (message instanceof Message) {
            Message msg = (Message) message;
            if (StringUtils.isBlank(msg.getKeys())) {
                msg.setKeys(IdWorker.nextStringId());
            }
        }
    }
}
