package com.weweibuy.framework.rocketmq.core.provider;

import org.apache.rocketmq.common.message.Message;

/**
 * 消息生产方法参数处理器
 *
 * @author durenhao
 * @date 2020/1/1 12:03
 **/
public interface MethodParameterProcessor {


    /**
     * 构建元数据
     *
     * @param methodMetadata
     * @param parameterType
     * @param argIndex
     */
    RocketMethodMetadata buildMetadata(RocketMethodMetadata methodMetadata, Class<?> parameterType, int argIndex);

    /**
     * 处理参数
     *
     * @param methodMetadata
     * @param message
     * @param args
     * @param index
     * @return
     */
    Message process(RocketMethodMetadata methodMetadata, Message message, Object[] args, int index);


}
