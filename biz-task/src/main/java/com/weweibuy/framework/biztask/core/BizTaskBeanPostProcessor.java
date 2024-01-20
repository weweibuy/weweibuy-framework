package com.weweibuy.framework.biztask.core;

import com.weweibuy.framework.biztask.annotation.ExecBizTask;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;

/**
 * @author durenhao
 * @date 2024/1/20 11:21
 **/
@RequiredArgsConstructor
public class BizTaskBeanPostProcessor implements BeanPostProcessor {

    private final BizTaskHandlerMethodHolder bizTaskHandlerMethodHolder;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        processExecBizTask(bean, beanName);

        return bean;
    }

    private ExecBizTask findListenerAnnotation(Class<?> clazz) {
        return AnnotationUtils.findAnnotation(clazz, ExecBizTask.class);
    }

    private void processExecBizTask(Object bean, String beanName) {
        ReflectionUtils.doWithLocalMethods(bean.getClass(), method -> {
            ExecBizTask annotation = method.getAnnotation(ExecBizTask.class);
            if (annotation != null) {
                bizTaskHandlerMethodHolder.buildHandlerMethod(bean, method, annotation);
            }
        });
    }


}
