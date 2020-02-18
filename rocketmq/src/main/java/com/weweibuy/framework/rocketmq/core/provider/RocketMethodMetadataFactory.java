package com.weweibuy.framework.rocketmq.core.provider;

import java.lang.reflect.Method;

/**
 * 元数据构建工厂
 *
 * @author durenhao
 * @date 2020/1/1 21:21
 **/
public interface RocketMethodMetadataFactory {


    RocketMethodMetadata newInstance(Class<?> target, Method method);


}
