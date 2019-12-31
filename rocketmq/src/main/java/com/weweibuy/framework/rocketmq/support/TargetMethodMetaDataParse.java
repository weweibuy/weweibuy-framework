package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.annotation.RocketProvider;
import com.weweibuy.framework.rocketmq.annotation.RocketProviderHandler;
import com.weweibuy.framework.rocketmq.core.RocketMethodMetadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 目标方法元数据解析器
 *
 * @author durenhao
 * @date 2019/12/30 20:47
 **/
public class TargetMethodMetaDataParse {


    public Map<Method, RocketMethodMetadata> parser(Class<?> target) {
        Class<?>[] interfaces = target.getInterfaces();
        RocketProvider annotation = interfaces[0].getAnnotation(RocketProvider.class);
        String topic = annotation.topic();

        HashMap<Method, RocketMethodMetadata> objectObjectHashMap = new HashMap<>();
        Arrays.stream(target.getMethods())
                .filter(m -> !shouldFilterMethod(m))
                .filter(m -> Objects.nonNull(m.getAnnotation(RocketProviderHandler.class)))
                .forEach(m -> {
                    RocketMethodMetadata rocketMethodMetadata = new RocketMethodMetadata();
                    RocketProviderHandler providerHandler = m.getAnnotation(RocketProviderHandler.class);
                    parseAnnotationOnMethod(rocketMethodMetadata, providerHandler);
                    Annotation[][] parameterAnnotations = m.getParameterAnnotations();
                });
        return null;

    }


    private RocketMethodMetadata parseAnnotationOnMethod(RocketMethodMetadata metadata, RocketProviderHandler providerHandler) {
        metadata.setTag(providerHandler.tag());
        metadata.setKeyExpression(providerHandler.key());
        metadata.setOneWay(providerHandler.oneWay());
        metadata.setOrderly(providerHandler.orderly());
        metadata.setTimeout(providerHandler.timeout());
        return metadata;
    }

    private void parseAnnotationOnParameter(RocketMethodMetadata metadata, Method method, Annotation[] annotations, int index) {
        for (int i = 0; i < annotations.length; i++) {
        }
    }


    private boolean shouldFilterMethod(Method method) {
        return method.getDeclaringClass() == Object.class ||
                (method.getModifiers() & Modifier.STATIC) != 0 ||
                MethodUtils.isDefault(method);
    }


    public static void main(String[] args) {
        Method[] methods = TargetMethodMetaDataParse.class.getDeclaredMethods();
        Arrays.stream(methods)
                .filter(method -> method.getName().equals("parseAnnotationOnParameter"))
                .findFirst()
                .ifPresent(m -> {
                    Annotation[][] parameterAnnotations = m.getParameterAnnotations();
                    Annotation[] parameterAnnotation = parameterAnnotations[0];

                    System.err.println(parameterAnnotations.length);
                    System.err.println(parameterAnnotation.length);
                });
    }

}
