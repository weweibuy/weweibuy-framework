package com.weweibuy.framework.rocketmq.config;

import com.weweibuy.framework.rocketmq.core.MessageConverter;
import com.weweibuy.framework.rocketmq.core.StringMessageConverter;
import com.weweibuy.framework.rocketmq.support.*;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/1/1 23:03
 **/
@Configuration
@EnableConfigurationProperties(RocketMqProperties.class)
@ConditionalOnProperty(prefix = "rocket-mq", name = {"name-server", "provider.group"})
public class ProviderConfig {

    private final RocketMqProperties rocketMqProperties;

    public ProviderConfig(RocketMqProperties rocketMqProperties) {
        this.rocketMqProperties = rocketMqProperties;
    }

    @Bean
    public ProxyRocketProvider proxyRocketProvider(List<AnnotatedParameterProcessor> parameterProcessorList, MessageConverter messageConverter) throws Exception {
        return new ProxyRocketProvider(messageQueueSelector(), targetMethodMetaDataParser(parameterProcessorList, messageConverter), mqProducer());
    }

    @Bean
    @ConditionalOnMissingBean(MessageQueueSelector.class)
    public MessageQueueSelector messageQueueSelector() {
        return new SelectMessageQueueByHash();
    }

    @Bean
    public TargetMethodMetaDataParser targetMethodMetaDataParser(List<AnnotatedParameterProcessor> parameterProcessorList, MessageConverter messageConverter) {
        return new TargetMethodMetaDataParser(rocketMethodMetadataFactory(), messageBodyParameterProcessor(messageConverter), parameterProcessorList);
    }

    @Bean
    public AnnotatedParameterProcessor tagParameterProcessor() {
        return new TagParameterProcessor();
    }

    @Bean
    public AnnotatedParameterProcessor payloadParameterProcessor(MessageConverter messageConverter) {
        return new PayloadParameterProcessor(messageConverter);
    }

    @Bean
    public AnnotatedParameterProcessor keyParameterProcessor() {
        return new KeyParameterProcessor();
    }

    @Bean
    public AnnotatedParameterProcessor headerParameterProcessor() {
        return new HeaderParameterProcessor();
    }

    @Bean
    public MessageBodyParameterProcessor messageBodyParameterProcessor(MessageConverter messageConverter) {
        return new MessageBodyParameterProcessor(messageConverter);
    }

    @Bean
    @ConditionalOnMissingBean(MessageConverter.class)
    public MessageConverter stringMessageConverter() {
        return new StringMessageConverter();
    }


    @Bean
    @ConditionalOnMissingBean(RocketMethodMetadataFactory.class)
    public RocketMethodMetadataFactory rocketMethodMetadataFactory() {
        return new DefaultRocketMethodMetadataFactory();
    }

    @Bean
    public MQProducer mqProducer() throws Exception {
        RocketMqProperties.Provider producerConfig = rocketMqProperties.getProvider();
        String nameServer = rocketMqProperties.getNameServer();
        String groupName = producerConfig.getGroup();
        Assert.hasText(nameServer, "[rocketmq.name-server] must not be null");
        Assert.hasText(groupName, "[rocketmq.producer.group] must not be null");

        AccessChannel accessChannel = producerConfig.getAccessChannel();

        DefaultMQProducer producer;
        producer = new DefaultMQProducer(groupName, rocketMqProperties.getProvider().getEnableMsgTrace(),
                rocketMqProperties.getProvider().getCustomizedTraceTopic());

        producer.setNamesrvAddr(nameServer);
        producer.setAccessChannel(accessChannel);
        producer.setSendMsgTimeout(producerConfig.getSendMessageTimeout());
        producer.setRetryTimesWhenSendFailed(producerConfig.getRetryTimesWhenSendFailed());
        producer.setRetryTimesWhenSendAsyncFailed(producerConfig.getRetryTimesWhenSendAsyncFailed());
        producer.setMaxMessageSize(producerConfig.getMaxMessageSize());
        producer.setCompressMsgBodyOverHowmuch(producerConfig.getCompressMessageBodyThreshold());
        producer.setRetryAnotherBrokerWhenNotStoreOK(producerConfig.getRetryNextServer());
        return producer;
    }

}
