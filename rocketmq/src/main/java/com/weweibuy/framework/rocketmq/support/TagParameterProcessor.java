package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.annotation.Tag;
import com.weweibuy.framework.rocketmq.core.provider.AnnotatedParameterProcessor;
import com.weweibuy.framework.rocketmq.core.provider.RocketMethodMetadata;
import org.apache.rocketmq.common.message.Message;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;

/**
 * @author durenhao
 * @date 2020/1/1 17:11
 **/
public class TagParameterProcessor implements AnnotatedParameterProcessor {

    private static final Class<Tag> ANNOTATION = Tag.class;

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

        Assert.isNull(methodMetadata.getTagIndex(), "方法: " + methodMetadata.getMethod().getDeclaringClass().getSimpleName() + "."
                + methodMetadata.getMethod().getName() + " 中有多个可以成为Tag的参数");

        Assert.isTrue(ClassUtils.isAssignable(String.class, parameterTypes), "方法: " + methodMetadata.getMethod().getDeclaringClass().getSimpleName() + "."
                + methodMetadata.getMethod().getName() + " Tag 必须为String 类型");

        methodMetadata.setTagIndex(argIndex);

        methodMetadata.getMethodParameterProcessorMap()
                .put(argIndex, this);

        return methodMetadata;
    }

    @Override
    public Message process(RocketMethodMetadata methodMetadata, Message message, Object[] args, int index) {
        message.setTags((String) args[index]);
        return message;
    }
}
