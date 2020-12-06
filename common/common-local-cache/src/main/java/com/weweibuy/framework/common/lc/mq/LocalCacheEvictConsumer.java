package com.weweibuy.framework.common.lc.mq;

import com.weweibuy.framework.common.lc.cache.LocalCacheService;
import com.weweibuy.framework.common.lc.mq.message.LocalCacheEvictMessage;
import com.weweibuy.framework.rocketmq.annotation.Payload;
import com.weweibuy.framework.rocketmq.annotation.RocketConsumerHandler;
import com.weweibuy.framework.rocketmq.annotation.RocketListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Optional;
import java.util.Set;

/**
 * 失效本地缓存消费者
 * 建议自行覆盖 group
 *
 * @author durenhao
 * @date 2020/12/6 11:35
 **/
@Slf4j
@RocketListener(topic = "${rocket-mq.local-cache-evict.topic}", group = "${rocket-mq.local-cache-evict.consumer-group}",
        messageModel = MessageModel.BROADCASTING, threadMax = 1, threadMin = 1)
public class LocalCacheEvictConsumer {

    @Autowired
    private LocalCacheService localCacheService;

    @Autowired
    private ApplicationContext applicationContext;


    @RocketConsumerHandler(tags = "${rocket-mq.local-cache-evict.tag}")
    public void onMessage(@Payload LocalCacheEvictMessage message) {
        String cacheName = message.getCacheName();
        Set<String> cacheKeyList = message.getCacheKeyList();
        localCacheService.evictCache(cacheName, cacheKeyList);
        log.warn("刷新缓存: cacheName: {}, cacheKey: {} 完成", cacheName, cacheKeyList);
        Optional.ofNullable(message.getCallBackEvent())
                .ifPresent(applicationContext::publishEvent);
    }


}
