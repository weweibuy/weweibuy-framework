package com.weweibuy.framework.common.lc.mq.message;

import lombok.Data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 失效本地缓存消息
 *
 * @author durenhao
 * @date 2020/12/6 11:33
 **/
@Data
public class LocalCacheEvictMessage {

    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * 缓存key
     */
    private Set<String> cacheKeyList;

    public static LocalCacheEvictMessage evict(String cacheName) {
        return evict(cacheName, null);
    }

    public static LocalCacheEvictMessage evict(String cacheName, Collection<String> cacheKeyList) {
        LocalCacheEvictMessage evictMessage = new LocalCacheEvictMessage();
        evictMessage.setCacheName(cacheName);
        if (cacheKeyList != null) {
            evictMessage.setCacheKeyList(new HashSet<>(cacheKeyList));
        } else {
            evictMessage.setCacheKeyList(Collections.emptySet());
        }
        return evictMessage;
    }

}
