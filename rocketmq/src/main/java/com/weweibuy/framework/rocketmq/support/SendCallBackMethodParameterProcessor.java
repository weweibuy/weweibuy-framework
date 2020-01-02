/*
 * All rights Reserved, Designed By baowei
 *
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.core.RocketMethodMetadata;
import org.apache.rocketmq.common.message.Message;
import org.springframework.util.Assert;

/**
 * @author durenhao
 * @date 2020/1/2 10:35
 **/
public class SendCallBackMethodParameterProcessor implements MethodParameterProcessor {


    @Override
    public RocketMethodMetadata buildMetadata(RocketMethodMetadata methodMetadata, Class<?> parameterType, int argIndex) {
        Assert.isNull(methodMetadata.getAsyncIndex(), "方法: " + methodMetadata.getMethod().getDeclaringClass().getSimpleName() + "."
                + methodMetadata.getMethod().getName() + " 中有多个  SendCallBack 参数");

        methodMetadata.getMethodParameterProcessorMap()
                .put(argIndex, this);

        methodMetadata.setAsyncIndex(argIndex);
        return methodMetadata;
    }

    @Override
    public Message process(RocketMethodMetadata methodMetadata, Message message, Object[] args, int index) {
        // do nothing
        return message;
    }
}
