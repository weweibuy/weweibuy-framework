package com.weweibuy.framework.rocketmq.config;

import lombok.Data;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.common.MixAll;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * rocketMQ 配置
 *
 * @author durenhao
 * @date 2020/1/1 23:08
 **/
@Data
@ConfigurationProperties(prefix = "rocket-mq")
public class RocketMqProperties {

    /**
     * nameServer 地址
     */
    private String nameServer;

    /**
     * 生产者配置
     */
    private Producer producer;

    /**
     * 消费者配置
     */
    private Map<String, Consumer> consumer;


    @Data
    public static class Producer {

        /**
         * 组名
         */
        private String group;

        private AccessChannel accessChannel = AccessChannel.LOCAL;

        /**
         * 发送消息超时时间，单位毫秒
         */
        private Integer sendMessageTimeout = 3000;

        /**
         * 消息Body超过多大开始压缩（Consumer收到消息会自动解压缩），单位字节
         */
        private Integer compressMessageBodyThreshold = 4096;

        /**
         * 同步发送模式 如果消息发送失败，最大重试次数
         */
        private Integer retryTimesWhenSendFailed = 2;

        /**
         * 异步发送模式: 如果消息发送失败，最大重试次数
         */
        private Integer retryTimesWhenSendAsyncFailed = 2;

        /**
         * 如果发送消息返回sendResult，但是sendStatus != SEND_OK，是否重试发送
         */
        private Boolean retryNextServer = false;

        /**
         * 客户端限制的消息大小，超过报错，同时服务端也会限制，所以需要跟服务端配合使用
         * 默认4M
         */
        private Integer maxMessageSize = 1024 * 1024 * 4;

        /**
         * ACL 访问控制  accessKey
         */
        private String accessKey;

        /**
         * ACL 访问控制  secretKey
         */
        private String secretKey;

        /**
         * 是否启用消息轨迹功能
         */
        private Boolean enableMsgTrace = false;

        /**
         * 保存消费轨迹的Topic
         */
        private String customizedTraceTopic = MixAll.RMQ_SYS_TRACE_TOPIC;

    }


    @Data
    public static class Consumer {

        /**
         * 是否开启效果轨迹
         */
        private Boolean enableMsgTrace = false;

        /**
         * 保存消费轨迹的Topic
         */
        private String customizedTraceTopic = MixAll.RMQ_SYS_TRACE_TOPIC;

        private AccessChannel accessChannel = AccessChannel.LOCAL;

        /**
         * 主题
         */
        private String topic;

        /**
         * 消费组
         */
        private String group;

        /**
         * tag
         */
        private String tags;

        /**
         * 最大的消费超时时间 单位分钟, 如果消费超时，RocketMQ会等同于消费失败来处理
         * https://zhuanlan.zhihu.com/p/25265380
         */
        private Long consumeTimeout;

        /**
         * 最小消费线程数
         */
        private Integer threadMin;

        /**
         * 最大消费线程数
         */
        private Integer threadMax;

        /**
         * ACL 访问控制  accessKey
         */
        private String accessKey;

        /**
         * ACL 访问控制  secretKey
         */
        private String secretKey;

    }
}
