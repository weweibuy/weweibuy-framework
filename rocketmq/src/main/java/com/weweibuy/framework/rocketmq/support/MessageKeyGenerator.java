package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.core.RocketMethodMetadata;

/**
 * messageKey 生成器
 *
 * @author durenhao
 * @date 2019/12/31 17:33
 **/
public interface MessageKeyGenerator {

    /**
     * Key生成器
     *
     * @param metadata
     * @return
     */
    String generatorKey(RocketMethodMetadata metadata, Object... args);


}
