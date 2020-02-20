package com.weweibuy.framework.samples.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weweibuy.framework.rocketmq.core.MessageConverter;
import com.weweibuy.framework.rocketmq.core.provider.MessageSendFilter;
import com.weweibuy.framework.rocketmq.support.JacksonRocketMqMessageConverter;
import com.weweibuy.framework.rocketmq.support.provider.LogMessageSendFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author durenhao
 * @date 2020/1/2 17:13
 **/
@Configuration
public class RocketMqMessageConverterConfig {


    @Bean
    public MessageConverter jacksonRocketMqMessageConverter(ObjectMapper objectMapper) {
        return new JacksonRocketMqMessageConverter(objectMapper);
    }

    @Bean
    public MessageSendFilter logMessageSendFilter() {
        return new LogMessageSendFilter();
    }

}
