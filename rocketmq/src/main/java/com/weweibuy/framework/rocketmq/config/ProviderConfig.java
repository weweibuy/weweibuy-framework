package com.weweibuy.framework.rocketmq.config;

import com.weweibuy.framework.rocketmq.core.MessageConverter;
import com.weweibuy.framework.rocketmq.core.provider.AnnotatedParameterProcessorComposite;
import com.weweibuy.framework.rocketmq.core.provider.MessageSendFilter;
import com.weweibuy.framework.rocketmq.core.provider.ProxyRocketProvider;
import com.weweibuy.framework.rocketmq.support.*;
import com.weweibuy.framework.rocketmq.utils.RocketMqUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author durenhao
 * @date 2020/1/1 23:03
 **/
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "rocket-mq", name = {"name-server", "provider.group"})
public class ProviderConfig {

    private final RocketMqProperties rocketMqProperties;

    @Autowired(required = false)
    private List<RocketConfigurer> configurer;

    public ProviderConfig(RocketMqProperties rocketMqProperties) {
        this.rocketMqProperties = rocketMqProperties;
    }

    @Bean
    public ProxyRocketProvider proxyRocketProvider(MessageConverter messageConverter) throws Exception {

        List<MessageSendFilter> sendFilterList = new ArrayList<>();

        if (configurer != null) {
            configurer.forEach(c -> c.addMessageSendFilter(sendFilterList));
        }

        return new ProxyRocketProvider(messageQueueSelector(), targetMethodMetaDataParser(messageConverter), mqProducer(), sendFilterList);
    }

    @Bean
    @ConditionalOnMissingBean(MessageQueueSelector.class)
    public MessageQueueSelector messageQueueSelector() {
        return new SelectMessageQueueByHash();
    }

    @Bean
    public TargetMethodMetaDataParser targetMethodMetaDataParser(MessageConverter messageConverter) {

        AnnotatedParameterProcessorComposite composite = new AnnotatedParameterProcessorComposite();
        composite.addProcessor(new TagParameterProcessor());
        composite.addProcessor(new PayloadParameterProcessor(messageConverter));
        composite.addProcessor(new KeyParameterProcessor());
        composite.addProcessor(new HeaderParameterProcessor());

        if (!CollectionUtils.isEmpty(configurer)) {
            configurer.forEach(c -> c.addAnnotatedParameterProcessor(composite));
        }


        return new TargetMethodMetaDataParser(messageBodyParameterProcessor(messageConverter), composite);
    }


    @Bean
    public MessageBodyParameterProcessor messageBodyParameterProcessor(MessageConverter messageConverter) {
        return new MessageBodyParameterProcessor(messageConverter);
    }


    @Bean
    public MQProducer mqProducer() {
        RocketMqProperties.Provider producerConfig = rocketMqProperties.getProvider();
        String nameServer = rocketMqProperties.getNameServer();
        String groupName = producerConfig.getGroup();
        Assert.hasText(nameServer, "[rocketmq.name-server] must not be null");
        Assert.hasText(groupName, "[rocketmq.producer.group] must not be null");

        AccessChannel accessChannel = producerConfig.getAccessChannel();

        DefaultMQProducer producer;

        String accessKey = rocketMqProperties.getProvider().getAccessKey();
        String secretKey = rocketMqProperties.getProvider().getSecretKey();
        if (RocketMqUtils.canUseAcl(accessKey, secretKey)) {
            AclClientRPCHook rpcHook = new AclClientRPCHook(new SessionCredentials(accessKey, secretKey));
            producer = new DefaultMQProducer(groupName, rpcHook, rocketMqProperties.getProvider().getEnableMsgTrace(),
                    rocketMqProperties.getProvider().getCustomizedTraceTopic());
        } else {
            producer = new DefaultMQProducer(groupName, rocketMqProperties.getProvider().getEnableMsgTrace(),
                    rocketMqProperties.getProvider().getCustomizedTraceTopic());
        }

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
