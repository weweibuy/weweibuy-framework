package com.weweibuy.framework.common.lc.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.lc.mq.LocalCacheEvictProducer;
import com.weweibuy.framework.common.lc.mq.message.LocalCacheEvictMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 规则元数据缓存管理
 *
 * @author durenhao
 * @date 2020/12/6 10:59
 **/
@Slf4j
public class LocalCacheService {

    @Autowired
    private CacheManager ruleMetaCache;

    @Autowired(required = false)
    private LocalCacheEvictProducer localCacheEvictProducer;

    public Map<String, List<String>> listCacheKey() {
        Collection<String> cacheNames = ruleMetaCache.getCacheNames();
        Map<String, List<String>> cacheKeyMap = new HashMap<>();

        for (String name : cacheNames) {
            Cache<Object, Object> nativeCache = (Cache<Object, Object>) ruleMetaCache.getCache(name).getNativeCache();
            List<String> keyList = nativeCache.asMap().keySet().stream()
                    .limit(1000)
                    .map(Object::toString).collect(Collectors.toList());
            cacheKeyMap.put(name, keyList);
        }
        return cacheKeyMap;
    }


    public void evictCache() {
        evictCache(null, null);
    }

    public void evictCache(String name) {
        evictCache(name, null);
    }

    public void evictCache(String name, Set<String> keySet) {
        Collection<String> cacheNames = ruleMetaCache.getCacheNames();
        if (StringUtils.isBlank(name)) {
            cacheNames.stream()
                    .map(cacheName -> (Cache<Object, Object>) ruleMetaCache.getCache(cacheName).getNativeCache())
                    .forEach(cache -> cache.invalidateAll());
            return;
        }

        if (!cacheNames.contains(name)) {
            log.warn("缓存: {}, 不存在", name);
            return;
        }
        Cache<Object, Object> cache = (Cache<Object, Object>) ruleMetaCache.getCache(name).getNativeCache();
        if (CollectionUtils.isEmpty(keySet)) {
            cache.invalidateAll();
        } else {
            keySet.stream().forEach(k -> cache.invalidate(k));
        }
    }

    public void evictClusterCache() {
        evictClusterCache(null);
    }

    public void evictClusterCache(String cacheName) {
        if (localCacheEvictProducer != null) {
            localCacheEvictProducer.send(LocalCacheEvictMessage.evict(cacheName));
        } else {
            throw Exceptions.business("没有Mq, 无法刷新集群本地缓存");
        }
    }
}
