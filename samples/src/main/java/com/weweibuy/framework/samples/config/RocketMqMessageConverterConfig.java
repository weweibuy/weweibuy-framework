package com.weweibuy.framework.samples.config;

import com.weweibuy.framework.rocketmq.config.RocketConfigurer;
import com.weweibuy.framework.rocketmq.core.consumer.ConsumerFilter;
import com.weweibuy.framework.rocketmq.core.producer.MessageSendFilter;
import com.weweibuy.framework.rocketmq.support.consumer.LogMessageConsumerFilter;
import com.weweibuy.framework.rocketmq.support.producer.AddMessageKeyFilter;
import com.weweibuy.framework.rocketmq.support.producer.LogMessageSendFilter;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/1/2 17:13
 **/
@Configuration
public class RocketMqMessageConverterConfig implements RocketConfigurer {


    @Override
    public void addConsumerFilter(List<ConsumerFilter> consumerFilter) {
        consumerFilter.add(new LogMessageConsumerFilter());
    }

    @Override
    public void addMessageSendFilter(List<MessageSendFilter> messageSendFilter) {
        messageSendFilter.add(logMessageSendFilter());
        messageSendFilter.add(addMessageKeyFilter());
    }


    public MessageSendFilter logMessageSendFilter() {
        return new LogMessageSendFilter();
    }

    public AddMessageKeyFilter addMessageKeyFilter() {
        return new AddMessageKeyFilter();
    }

}
