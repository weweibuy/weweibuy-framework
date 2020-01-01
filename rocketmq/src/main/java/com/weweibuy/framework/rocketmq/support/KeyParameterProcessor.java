package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.annotation.Key;
import com.weweibuy.framework.rocketmq.core.RocketMethodMetadata;
import org.apache.rocketmq.common.message.Message;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;

/**
 * @author durenhao
 * @date 2020/1/1 17:18
 **/
public class KeyParameterProcessor implements AnnotatedParameterProcessor {

    private static final Class<Key> ANNOTATION = Key.class;

    private final MessageKeyGenerator messageKeyGenerator = new AnnotationMessageKeyGenerator();

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


        methodMetadata.getMethodParameterProcessorMap()
                .put(argIndex, this);

        methodMetadata.setKeyIndex(argIndex);
        methodMetadata.setMessageKeyGenerator(messageKeyGenerator);

        return methodMetadata;
    }

    @Override
    public Message process(RocketMethodMetadata methodMetadata, Message message, Object[] args, int index) {
        methodMetadata.getMessageKeyGenerator()
                .generatorKey(methodMetadata, args);
        return message;
    }
}
