package com.weweibuy.framework.rocketmq.config;

import lombok.Data;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.common.MixAll;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author durenhao
 * @date 2020/1/1 23:08
 **/
@Data
@ConfigurationProperties(prefix = "rocket-mq")
public class RocketMqProperties {

    private String nameServer;

    private Provider provider;

    private Map<String, Consumer> consumer;


    @Data
    public static class Provider {

        private String group;

        private AccessChannel accessChannel = AccessChannel.LOCAL;

        private Integer sendMessageTimeout = 3000;

        private Integer compressMessageBodyThreshold = 1024 * 4;

        private Integer retryTimesWhenSendFailed = 2;

        private Integer retryTimesWhenSendAsyncFailed = 2;

        private Boolean retryNextServer = false;

        private Integer maxMessageSize = 1024 * 1024 * 4;

        private String accessKey;

        private String secretKey;

        private Boolean enableMsgTrace = true;

        private String customizedTraceTopic = MixAll.RMQ_SYS_TRACE_TOPIC;

    }


    @Data
    public static class Consumer {

        private Boolean enableMsgTrace = false;

        private String customizedTraceTopic;

        private AccessChannel accessChannel = AccessChannel.LOCAL;

        private String topic;

        private String group;

        private Long timeout;

        private Integer maxRetry;

        private Integer threadMin;

        private Integer threadMax;

        private String accessKey;

        private String secretKey;

    }
}
