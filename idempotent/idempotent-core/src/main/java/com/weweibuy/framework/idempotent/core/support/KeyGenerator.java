package com.weweibuy.framework.idempotent.core.support;

/**
 * 幂等key 生成器
 *
 * @author durenhao
 * @date 2020/6/19 22:33
 **/
public interface KeyGenerator {

    /**
     * 生成 幂等key
     *
     * @param key 被自动解析一层之后的 key
     * @return
     */
    String generatorKey(String key);

}
