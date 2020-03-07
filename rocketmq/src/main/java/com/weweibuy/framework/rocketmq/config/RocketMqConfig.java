package com.weweibuy.framework.rocketmq.config;

import com.weweibuy.framework.rocketmq.core.MessageConverter;
import com.weweibuy.framework.rocketmq.support.JacksonRocketMqMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

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
    public MessageConverter stringMessageConverter() {
        return new JacksonRocketMqMessageConverter();
    }


}
