package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.annotation.Header;
import com.weweibuy.framework.rocketmq.core.provider.AnnotatedParameterProcessor;
import com.weweibuy.framework.rocketmq.core.provider.RocketMethodMetadata;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.TypeUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author durenhao
 * @date 2020/1/1 17:33
 **/
public class HeaderParameterProcessor implements AnnotatedParameterProcessor {

    private static final Class<Header> ANNOTATION = Header.class;

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
        Annotation[][] parameterAnnotations = methodMetadata.getMethod().getParameterAnnotations();
        Annotation[] parameterAnnotation = parameterAnnotations[argIndex];
        Header header = Arrays.stream(parameterAnnotation)
                .filter(ANNOTATION::isInstance)
                .map(a -> (Header) a)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("方法: " + methodMetadata.getMethod().getDeclaringClass().getSimpleName() + "."
                        + methodMetadata.getMethod().getName() + " @Header 参数错误"));

        String value = header.value();
        Map<Integer, String> headerIndexName = methodMetadata.getHeaderIndexName();
        if (headerIndexName == null) {
            headerIndexName = new HashMap<>();
        }

        if (StringUtils.isNotBlank(value)) {

            Assert.isTrue(!MessageConst.STRING_HASH_SET.contains(value), String.format(
                    "The Property<%s> is used by system, input another please", value));

            headerIndexName.put(argIndex, value);
        } else {
            if (!ClassUtils.isAssignable(Map.class, parameterTypes) || isStringMap(methodMetadata, argIndex)) {
                throw new IllegalArgumentException("方法: " + methodMetadata.getMethod().getDeclaringClass().getSimpleName() + "."
                        + methodMetadata.getMethod().getName() + " @Header 不指定value时参数必须是 Map<String, String> 类型");
            }

        }
        methodMetadata.getMethodParameterProcessorMap()
                .put(argIndex, this);
        return methodMetadata;
    }

    @Override
    public Message process(RocketMethodMetadata methodMetadata, Message message, Object[] args, int index) {
        Object arg = args[index];
        if (arg instanceof Map) {
            ((Map) arg).forEach((k, v) -> message.putUserProperty((String) k, (String) v));
        } else {
            String name = methodMetadata.getHeaderIndexName().get(index);
            message.putUserProperty(name, arg + "");
        }
        return message;
    }


    private boolean isStringMap(RocketMethodMetadata methodMetadata, int index) {
        Type type = methodMetadata.getMethod().getGenericParameterTypes()[index];

        if (!(type instanceof ParameterizedType)) {
            throw new IllegalArgumentException("方法: " + methodMetadata.getMethod().getDeclaringClass().getSimpleName() + "."
                    + methodMetadata.getMethod().getName() + " @Header 不指定value时参数必须是 Map<String, String> 类型");
        }
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();

        return actualTypeArguments.length == 2 && TypeUtils.isAssignable(String.class, actualTypeArguments[0])
                && TypeUtils.isAssignable(String.class, actualTypeArguments[1]);
    }
}
