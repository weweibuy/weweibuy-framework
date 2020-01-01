package com.weweibuy.framework.rocketmq.support;

import com.weweibuy.framework.rocketmq.annotation.Payload;
import com.weweibuy.framework.rocketmq.core.MessageConverter;

import java.lang.annotation.Annotation;

/**
 * @author durenhao
 * @date 2020/1/1 16:55
 **/
public class PayloadParameterProcessor extends MessageBodyParameterProcessor implements AnnotatedParameterProcessor {

    private static final Class<Payload> ANNOTATION = Payload.class;

    public PayloadParameterProcessor(MessageConverter messageConverter) {
        super(messageConverter);
    }

    @Override
    public boolean match(Annotation annotation) {
        return ANNOTATION.isInstance(annotation);
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return ANNOTATION;
    }


}
