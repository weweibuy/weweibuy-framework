package com.weweibuy.framework.rocketmq.config;

import lombok.Data;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.common.MixAll;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author durenhao
 * @date 2020/1/1 23:08
 **/
@Data
@ConfigurationProperties(prefix = "rocket-mq")
public class RocketMqProperties {

    private String nameServer;

    private AccessChannel accessChannel = AccessChannel.LOCAL;

    private Provider provider;


    @Data
    public static class Provider {

        private String group;

        private int sendMessageTimeout = 3000;

        private int compressMessageBodyThreshold = 1024 * 4;

        private int retryTimesWhenSendFailed = 2;

        private int retryTimesWhenSendAsyncFailed = 2;

        private boolean retryNextServer = false;

        private int maxMessageSize = 1024 * 1024 * 4;

        private String accessKey;

        private String secretKey;

        private boolean enableMsgTrace = true;

        private String customizedTraceTopic = MixAll.RMQ_SYS_TRACE_TOPIC;


    }
}
