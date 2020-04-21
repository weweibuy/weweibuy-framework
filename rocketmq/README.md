# rocketmq
  基于Spring5 对rockerMq,Java 客户端的封装,已服务化的形式发送MQ消息,便于在业务系统中快速集成rocketMq
  

## 支持功能
 同步发送  
 异步发送  
 one-way发送  
 发送顺序消息  
 批量发送  
 并发消费(广播/集群)
 顺序消费  
 TAG过滤  
 批量消费  
 
## 使用方法
  添加maven依赖：`maven:`
```
<dependency>
    <groupId>com.weweibuy.framework</groupId>
    <artifactId>rocketmq</artifactId>
</dependency>
```
  配置类上开启:
```
@EnableRocket
```

### 1.发送消息
  修改配置: 
`application.properites:`
```
rocket-mq.name-server = localhost:9876
rocket-mq.provider.group = MY_PROVIDER_GROUP
```
 示例: 
```java
package com.weweibuy.framework.samples.mq.provider;

import com.weweibuy.framework.rocketmq.annotation.Key;
import com.weweibuy.framework.rocketmq.annotation.RocketProvider;
import com.weweibuy.framework.rocketmq.annotation.RocketProviderHandler;
import com.weweibuy.framework.rocketmq.annotation.Tag;
import com.weweibuy.framework.rocketmq.support.JacksonRocketMqMessageConverter;
import com.weweibuy.framework.samples.message.SampleUser;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;

import java.util.Collection;

/**
 * @author durenhao
 * @date 2019/12/29 10:26
 **/
@RocketProvider(topic = "${rocket-mq.provider.sample-provider.topic}") // TOPIC 支持EL 表达式的形式
public interface SampleProvider {

    /**
     * TAG支持EL表达式的形式, 如何方法参数中有@Tag标记的值, 将覆盖注解中的值
     *
     * @param user 消息体, 将被 {@link JacksonRocketMqMessageConverter} 转化
     * @param s    TAG
     * @param key  messageKey
     * @return
     */
    @RocketProviderHandler(tag = "${rocket-mq.provider.sample-provider.tag}")
    SendResult send(SampleUser user, @Tag String s, @Key String key);

    /**
     * 顺序发送 使用 {@link SelectMessageQueueByHash}
     *
     * @param user         消息体
     * @param sendCallback 回调
     * @param key          messageKey
     * @return
     */
    @RocketProviderHandler(tag = "TEST_TAG", orderly = true)
    void sendAsync(SampleUser user, SendCallback sendCallback, @Key String key);

    /**
     * 批量发送
     *
     * @param users 消息体为 SampleUser, 参数必须为 Collection的形式
     * @return
     */
    @RocketProviderHandler(tag = "BBB", batch = true)
    SendResult sendBatch(Collection<SampleUser> users);

    /**
     * oneWay 发送
     *
     * @param user
     */
    @RocketProviderHandler(tag = "CCC", oneWay = true)
    void sendOneWay(SampleUser user);
}

```

#### 1.1 发送消息过滤器
  实现:[MessageSendFilter](src/main/java/com/weweibuy/framework/rocketmq/core/provider/MessageSendFilter.java)通过:
  [RocketConfigurer](src/main/java/com/weweibuy/framework/rocketmq/config/RocketConfigurer.java)配置自定义过滤器.
  示例: [RocketMqMessageConverterConfig#addMessageSendFilter](../samples/src/main/java/com/weweibuy/framework/samples/config/RocketMqMessageConverterConfig.java)


### 2.接收消息
  Concurrently消费示例:
```java
package com.weweibuy.framework.samples.mq.consumer;

import com.weweibuy.framework.rocketmq.annotation.Header;
import com.weweibuy.framework.rocketmq.annotation.Payload;
import com.weweibuy.framework.rocketmq.annotation.RocketConsumerHandler;
import com.weweibuy.framework.rocketmq.annotation.RocketListener;
import com.weweibuy.framework.samples.message.SampleDog;
import com.weweibuy.framework.samples.message.SampleUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author durenhao
 * @date 2019/12/29 10:26
 **/
@Slf4j
@Component
@RocketListener(topic = "TEST_SAMPLE_01", group = "TEST_SAMPLE_01_C_GROUP") // 定义一个RocketListener topic  group 支持EL表示式
public class SampleConsumer {

    /**
     * 接受消息, Tag为 QQQ
     *
     * @param user    @Payload 标记消息体
     * @param context
     */
    @RocketConsumerHandler(tags = "QQQ")
    public void onMessage(@Payload SampleUser user, ConsumeConcurrentlyContext context) {
        log.info("收到消息: {}, \r\n {} ", user, context);
    }

    /**
     * @param user      消息体
     * @param tag       @Header 获取消息属性
     * @param headerMap 消息属性
     */
    @RocketConsumerHandler(tags = "AAA")
    public void onMessage2(@Payload SampleUser<SampleDog> user, @Header(MessageConst.PROPERTY_TAGS) String tag, @Header Map<String, String> headerMap) {
        log.info("收到消息: {}", user);
    }

    /**
     * 多个TAG
     *
     * @param user
     */
    @RocketConsumerHandler(tags = "BBB||CCC")
    public void onMessage3(@Payload SampleUser<SampleDog> user) {
        log.info("收到消息: {}", user);
    }

    /**
     * 直接获取原始消息
     *
     * @param messageExt
     */
    @RocketConsumerHandler(tags = "DDD")
    public void onMessage5(MessageExt messageExt) {
        log.info("收到消息: {}", messageExt);
    }

}

```
 顺序消费:
```java
package com.weweibuy.framework.samples.mq.consumer;

import com.weweibuy.framework.rocketmq.annotation.Payload;
import com.weweibuy.framework.rocketmq.annotation.RocketConsumerHandler;
import com.weweibuy.framework.rocketmq.annotation.RocketListener;
import com.weweibuy.framework.samples.message.SampleUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author durenhao
 * @date 2020/3/20 11:05
 **/
@Slf4j
@Component
@RocketListener(topic = "TEST_SAMPLE_ORDERLY_01", group = "TEST_SAMPLE_ORDERLY_01_C_GROUP", orderly = true)
public class SampleOrderlyConsumer {

    /**
     * 顺序消费
     *
     * @param user
     */
    @RocketConsumerHandler(tags = "QQQ")
    public void onMessage(@Payload SampleUser user) {
        log.info("收到消息: {}, \r\n {} ", user);
    }

}
```

 批量消费:
```java
package com.weweibuy.framework.samples.mq.consumer;

import com.weweibuy.framework.rocketmq.annotation.*;
import com.weweibuy.framework.samples.message.SampleDog;
import com.weweibuy.framework.samples.message.SampleUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author durenhao
 * @date 2019/12/29 10:26
 **/
@Slf4j
@Component
@RocketListener(topic = "${rocket-mq.consumer.batch-consumer.topic}", group = "TEST_BATCH_SAMPLE_01_C_GROUP", consumeMessageBatchMaxSize = 50)
public class BatchSampleConsumer {


    @RocketConsumerHandler(tags = "AAA||BBB||CCC", batchHandlerModel = BatchHandlerModel.TOGETHER)
    public void onMessage(@Payload Collection<SampleUser<SampleDog>> user) {
        log.info("收到消息: {}", user);
    }


}
```

#### 2.1 全局异常处理: 
  实现:[RocketListenerErrorHandler](src/main/java/com/weweibuy/framework/rocketmq/core/consumer/RocketListenerErrorHandler.java)接口,交给Spring管理

#### 2.2 消费过滤器:
  示例: [RocketMqMessageConverterConfig#addConsumerFilter](../samples/src/main/java/com/weweibuy/framework/samples/config/RocketMqMessageConverterConfig.java)