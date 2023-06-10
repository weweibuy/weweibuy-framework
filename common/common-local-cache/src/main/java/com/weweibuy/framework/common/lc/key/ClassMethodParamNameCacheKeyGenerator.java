package com.weweibuy.framework.common.lc.key;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * key 生成器
 *
 * @author durenhao
 * @date 2020/11/28 11:24
 **/

public class ClassMethodParamNameCacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return generateKey(target, method, params);
    }

    public static Object generateKey(Object target, Method method, Object... params) {
        if (params.length == 0) {
            return new CacheSimpleKey(target.getClass(), method, null);
        }
        return new CacheSimpleKey(target.getClass(), method, params);
    }

}
