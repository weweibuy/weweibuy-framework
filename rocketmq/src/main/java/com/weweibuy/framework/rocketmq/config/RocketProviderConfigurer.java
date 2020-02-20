package com.weweibuy.framework.rocketmq.config;

import com.weweibuy.framework.rocketmq.core.provider.AnnotatedParameterProcessorComposite;

/**
 *
 * rocket 生产者配置
 *
 * @author durenhao
 * @date 2020/2/20 21:44
 **/
public interface RocketProviderConfigurer {


    default void addAnnotatedParameterProcessor(AnnotatedParameterProcessorComposite composite) {
    }

}
