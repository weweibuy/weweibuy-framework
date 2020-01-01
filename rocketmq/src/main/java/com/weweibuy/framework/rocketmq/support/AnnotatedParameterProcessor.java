package com.weweibuy.framework.rocketmq.support;

import java.lang.annotation.Annotation;

/**
 * 参数注解解析器
 *
 * @author durenhao
 * @date 2019/12/31 16:46
 **/
public interface AnnotatedParameterProcessor extends MethodParameterProcessor {

    /**
     * 是否匹配
     *
     * @param annotation
     * @return
     */
    boolean match(Annotation annotation);

    /**
     * 获取注解
     *
     * @return
     */
    Class<? extends Annotation> getAnnotation();
}
