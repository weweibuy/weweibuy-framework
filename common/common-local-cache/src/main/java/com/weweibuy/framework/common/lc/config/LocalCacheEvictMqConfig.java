package com.weweibuy.framework.common.lc.config;

import com.weweibuy.framework.common.lc.mq.LocalCacheEvictConsumer;
import com.weweibuy.framework.rocketmq.config.ConsumerConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MQ 刷新本地缓存配置
 *
 * @author durenhao
 * @date 2020/12/6 12:20
 **/
@AutoConfiguration(after = CaffeineCacheManager.class)
@ConditionalOnClass(name = "com.weweibuy.framework.rocketmq.config.ConsumerConfig")
@ConditionalOnProperty(name = "common.local-cache.mq-evict.enable", havingValue = "true")
public class LocalCacheEvictMqConfig {

    @Bean
    @ConditionalOnBean(value = {CacheManager.class, ConsumerConfig.class})
    public LocalCacheEvictConsumer localCacheEvictConsumer() {
        return new LocalCacheEvictConsumer();
    }
}
