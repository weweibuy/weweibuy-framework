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
