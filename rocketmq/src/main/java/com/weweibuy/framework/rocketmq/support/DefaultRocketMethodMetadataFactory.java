package com.weweibuy.framework.rocketmq.support;


import com.weweibuy.framework.rocketmq.core.provider.RocketMethodMetadata;
import com.weweibuy.framework.rocketmq.core.provider.RocketMethodMetadataFactory;

import java.lang.reflect.Method;

/**
 * @author durenhao
 * @date 2020/1/1 21:24
 **/
public class DefaultRocketMethodMetadataFactory implements RocketMethodMetadataFactory {


    @Override
    public RocketMethodMetadata newInstance(Class<?> target, Method method) {
        return new RocketMethodMetadata();
    }


}
