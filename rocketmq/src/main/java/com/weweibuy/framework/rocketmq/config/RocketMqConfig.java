package com.weweibuy.framework.rocketmq.config;

import com.weweibuy.framework.rocketmq.core.MessageConverter;
import com.weweibuy.framework.rocketmq.core.consumer.ConsumerFilter;
import com.weweibuy.framework.rocketmq.core.producer.MessageSendFilter;
import com.weweibuy.framework.rocketmq.support.JacksonRocketMqMessageConverter;
import com.weweibuy.framework.rocketmq.support.consumer.LogMessageConsumerFilter;
import com.weweibuy.framework.rocketmq.support.consumer.TraceCodeMessageConsumerFilter;
import com.weweibuy.framework.rocketmq.support.producer.AddMessageKeyFilter;
import com.weweibuy.framework.rocketmq.support.producer.LogMessageSendFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/3/7 21:07
 **/
@Configuration
@EnableConfigurationProperties(RocketMqProperties.class)
public class RocketMqConfig {

    @Bean
    @DependsOn("jackJsonUtils")
    @ConditionalOnMissingBean(MessageConverter.class)
    public MessageConverter jacksonRocketMqMessageConverter() {
        return new JacksonRocketMqMessageConverter();
    }

    @Configuration
    @ConditionalOnProperty(prefix = "rocket-mq.log", name = "enable", havingValue = "true", matchIfMissing = true)
    public static class RocketMqMessageLogConfig implements RocketConfigurer {


        @Override
        public void addConsumerFilter(List<ConsumerFilter> consumerFilter) {
            consumerFilter.add(new LogMessageConsumerFilter());
            consumerFilter.add(new TraceCodeMessageConsumerFilter());
        }

        @Override
        public void addMessageSendFilter(List<MessageSendFilter> messageSendFilter) {
            messageSendFilter.add(new LogMessageSendFilter());
            messageSendFilter.add(new AddMessageKeyFilter());
        }


    }
}