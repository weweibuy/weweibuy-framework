package com.weweibuy.framework.common.lc.config;

import com.weweibuy.framework.common.lc.cache.LocalCacheService;
import com.weweibuy.framework.common.lc.endpoint.LocalCacheEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 本地缓存配置
 *
 * @author durenhao
 * @date 2020/12/6 11:26
 **/
@Configuration
public class LocalCacheConfig {

    @Bean
    @ConditionalOnBean(CacheManager.class)
    public LocalCacheEndpoint localCacheCacheEndpoint() {
        return new LocalCacheEndpoint();
    }

    @Bean
    @ConditionalOnBean(CacheManager.class)
    public LocalCacheService localCacheService() {
        return new LocalCacheService();
    }

}
