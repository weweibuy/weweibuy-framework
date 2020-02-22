package com.weweibuy.framework.samples.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weweibuy.framework.rocketmq.config.RocketConfigurer;
import com.weweibuy.framework.rocketmq.core.MessageConverter;
import com.weweibuy.framework.rocketmq.core.consumer.ConsumerFilter;
import com.weweibuy.framework.rocketmq.core.provider.MessageSendFilter;
import com.weweibuy.framework.rocketmq.support.JacksonRocketMqMessageConverter;
import com.weweibuy.framework.rocketmq.support.consumer.LogMessageConsumerFilter;
import com.weweibuy.framework.rocketmq.support.provider.LogMessageSendFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/1/2 17:13
 **/
@Configuration
public class RocketMqMessageConverterConfig implements RocketConfigurer {


    @Bean
    public MessageConverter jacksonRocketMqMessageConverter(ObjectMapper objectMapper) {
        return new JacksonRocketMqMessageConverter(objectMapper);
    }

    @Override
    public void addConsumerFilter(List<ConsumerFilter> consumerFilter) {
        consumerFilter.add(new LogMessageConsumerFilter());
    }

    @Override
    public void addMessageSendFilter(List<MessageSendFilter> messageSendFilter) {
        messageSendFilter.add(logMessageSendFilter());
    }


    public MessageSendFilter logMessageSendFilter() {
        return new LogMessageSendFilter();
    }

}
