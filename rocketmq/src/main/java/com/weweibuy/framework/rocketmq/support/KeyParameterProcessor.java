package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.annotation.Key;
import com.weweibuy.framework.rocketmq.core.RocketMethodMetadata;
import org.apache.rocketmq.common.message.Message;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author durenhao
 * @date 2020/1/1 17:18
 **/
public class KeyParameterProcessor implements AnnotatedParameterProcessor {

    private static final Class<Key> ANNOTATION = Key.class;

    @Override
    public boolean match(Annotation annotation) {
        return ANNOTATION.isInstance(annotation);
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return ANNOTATION;
    }

    @Override
    public RocketMethodMetadata buildMetadata(RocketMethodMetadata methodMetadata, Class<?> parameterTypes, int argIndex) {

        Assert.isNull(methodMetadata.getKeyIndex(), "方法: " + methodMetadata.getMethod().getDeclaringClass().getSimpleName() + "."
                + methodMetadata.getMethod().getName() + " 中有多个可以成为Key的参数");

        Assert.isTrue(ClassUtils.isAssignable(String.class, parameterTypes) || ClassUtils.isAssignable(Collection.class, parameterTypes), "方法: " + methodMetadata.getMethod().getDeclaringClass().getSimpleName() + "."
                + methodMetadata.getMethod().getName() + " Key 必须为String或Collection 类型");


        methodMetadata.getMethodParameterProcessorMap()
                .put(argIndex, this);

        methodMetadata.setKeyIndex(argIndex);

        return methodMetadata;
    }

    @Override
    public Message process(RocketMethodMetadata methodMetadata, Message message, Object[] args, int index) {
        if (args[index] instanceof Collection) {
            message.setKeys((Collection) args[index]);
        } else {
            message.setKeys((String) args[index]);
        }
        return message;
    }
}
