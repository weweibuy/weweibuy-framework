package com.weweibuy.framework.idempotent.core.aspect;

import com.weweibuy.framework.idempotent.core.annotation.Idempotent;
import com.weweibuy.framework.idempotent.core.support.AnnotationMetaDataHolder;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

/**
 * 幂等切点
 *
 * @author durenhao
 * @date 2020/3/31 14:54
 **/
public class IdempotentPointcut extends StaticMethodMatcherPointcut {

    private AnnotationMetaDataHolder annotationMetaDataHolder;

    public IdempotentPointcut(AnnotationMetaDataHolder annotationMetaDataHolder) {
        this.annotationMetaDataHolder = annotationMetaDataHolder;
    }

    @Override
    public boolean matches(Method method, Class<?> aClass) {
        Idempotent annotation = method.getAnnotation(Idempotent.class);
        boolean match = annotation != null;
        if (match) {
            // TODO 获取真实方法 而非代理方法?
            annotationMetaDataHolder.putMetaData(method, annotation);
        }
        return match;
    }
}
