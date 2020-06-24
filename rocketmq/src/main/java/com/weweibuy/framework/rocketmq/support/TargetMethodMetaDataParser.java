package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.common.core.utils.MethodUtils;
import com.weweibuy.framework.rocketmq.annotation.RocketProducer;
import com.weweibuy.framework.rocketmq.annotation.RocketProducerHandler;
import com.weweibuy.framework.rocketmq.core.producer.AnnotatedParameterProcessor;
import com.weweibuy.framework.rocketmq.core.producer.AnnotatedParameterProcessorComposite;
import com.weweibuy.framework.rocketmq.core.producer.RocketMethodMetadata;
import com.weweibuy.framework.rocketmq.core.producer.RocketMethodMetadataFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 目标方法元数据解析器
 *
 * @author durenhao
 * @date 2019/12/30 20:47
 **/
public class TargetMethodMetaDataParser implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    private final RocketMethodMetadataFactory methodMetadataFactory = new DefaultRocketMethodMetadataFactory();

    private final MessageBodyParameterProcessor methodParameterProcessor;

    private final SendCallBackMethodParameterProcessor callBackMethodParameterProcessor = new SendCallBackMethodParameterProcessor();

    private final List<AnnotatedParameterProcessor> annotatedParameterProcessor;

    public TargetMethodMetaDataParser(MessageBodyParameterProcessor methodParameterProcessor,
                                      AnnotatedParameterProcessorComposite composite) {
        this.methodParameterProcessor = methodParameterProcessor;
        this.annotatedParameterProcessor = composite.getParameterProcessors();
    }


    public Map<Method, RocketMethodMetadata> parser(Class<?> target) {

        return Arrays.stream(target.getMethods())
                .filter(m -> !shouldFilterMethod(m))
                .filter(m -> Objects.nonNull(m.getAnnotation(RocketProducerHandler.class)))
                .map(m -> {
                    RocketMethodMetadata rocketMethodMetadata = methodMetadataFactory.newInstance(target, m);
                    parseAnnotationOnClass(rocketMethodMetadata, target);
                    parseAnnotationOnMethod(rocketMethodMetadata, m);
                    parseAnnotationOnParameter(rocketMethodMetadata, m);
                    return rocketMethodMetadata;
                })
                .peek(this::validateMeta)
                .collect(Collectors.toMap(RocketMethodMetadata::getMethod, i -> i));

    }

    private void validateMeta(RocketMethodMetadata metadata) {
        Assert.isTrue(metadata.getBodyIndex() != null, metadata.getMethod().getName() + ", 无法匹配消息体!");

        Assert.isTrue(!(metadata.getOneWay() && metadata.getAsyncIndex() != null), metadata.getMethod().toString() + ", oneWay发送不支持回调!");

        Assert.isTrue(!(metadata.getOrderly() && metadata.getKeyIndex() == null), metadata.getMethod().toString() + ", 顺序发送必须指定Key!");

        Assert.isTrue(!(metadata.getBatch() && metadata.getOrderly()), metadata.getMethod().toString() + ", 批量消息目前不支持顺序发送");

        Assert.isTrue(!(metadata.getBatch() && metadata.getAsyncIndex() != null), metadata.getMethod().toString() + ", 批量消息不支持异步回调");

        Assert.isTrue(!(metadata.getBatch() && metadata.getOneWay()), metadata.getMethod().toString() + ", 批量消息oneWay 发送");



    }

    protected RocketMethodMetadata parseAnnotationOnClass(RocketMethodMetadata methodMetadata, Class<?> target) {
        RocketProducer annotation = target.getAnnotation(RocketProducer.class);
        String topic = annotation.topic();
        methodMetadata.setTopic(resolve(topic));
        return methodMetadata;
    }

    protected RocketMethodMetadata parseAnnotationOnMethod(RocketMethodMetadata metadata, Method method) {
        RocketProducerHandler providerHandler = method.getAnnotation(RocketProducerHandler.class);
        metadata.setMethod(method);
        metadata.setTag(resolve(providerHandler.tag()));
        metadata.setOneWay(providerHandler.oneWay());
        metadata.setOrderly(providerHandler.orderly());
        metadata.setTimeout(providerHandler.timeout());
        metadata.setBatch(providerHandler.batch());
        return metadata;
    }


    private RocketMethodMetadata parseAnnotationOnParameter(RocketMethodMetadata metadata, Method method, Annotation[] annotations, int index) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (annotations.length == 0) {
            if (ClassUtils.isAssignable(SendCallback.class, parameterTypes[index])) {
                callBackMethodParameterProcessor.buildMetadata(metadata, parameterTypes[index], index);
            } else {
                methodParameterProcessor.buildMetadata(metadata, parameterTypes[index], index);
            }
        } else {
            boolean annotationsMatched = false;
            for (int i = 0; i < annotations.length; i++) {
                for (int j = 0; j < annotatedParameterProcessor.size(); j++) {
                    if (annotatedParameterProcessor.get(j).match(annotations[i])) {
                        Assert.isTrue(!annotationsMatched, method.getName() + " 第" + index + "参数,匹配多个处理器!");
                        annotatedParameterProcessor.get(j).buildMetadata(metadata, parameterTypes[index], index);
                        annotationsMatched = true;
                    }
                }
            }
            Assert.isTrue(annotationsMatched, method.getName() + " 第" + index + "参数,无法匹配处理!");
        }
        return metadata;
    }


    protected RocketMethodMetadata parseAnnotationOnParameter(RocketMethodMetadata metadata, Method method) {
        Annotation[][] annotations = method.getParameterAnnotations();
        Assert.isTrue(annotations.length != 0, "方法: " + metadata.getMethod().getDeclaringClass().getSimpleName() + "."
                + metadata.getMethod().getName() + " 方法不能为空参");
        for (int i = 0; i < annotations.length; i++) {
            parseAnnotationOnParameter(metadata, method, annotations[i], i);
        }
        return metadata;
    }


    private boolean shouldFilterMethod(Method method) {
        return method.getDeclaringClass() == Object.class ||
                (method.getModifiers() & Modifier.STATIC) != 0 ||
                MethodUtils.isDefault(method);
    }


    private String resolve(String value) {
        if (StringUtils.isNoneBlank(value)
                && this.resourceLoader instanceof ConfigurableApplicationContext) {
            return ((ConfigurableApplicationContext) this.resourceLoader).getEnvironment()
                    .resolvePlaceholders(value);
        }
        return value;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
