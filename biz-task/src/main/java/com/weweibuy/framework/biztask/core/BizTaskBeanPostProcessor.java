package com.weweibuy.framework.biztask.core;

import com.weweibuy.framework.biztask.annotation.ExecBizTask;
import com.weweibuy.framework.biztask.db.po.BizTask;
import com.weweibuy.framework.common.core.utils.MethodUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;


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
                validateMethod(method);
                bizTaskHandlerMethodHolder.buildHandlerMethod(bean, method, annotation);
            }
        });
    }

    private void validateMethod(Method method) {
        boolean isPublic = MethodUtils.isPublic(method);
        if (!isPublic) {
            throw new IllegalArgumentException("method: " + method.getDeclaringClass().getName() + "." + method.getName() + " 不是 public方法, 无法执行业务任务");
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes == null || parameterTypes.length != 1 || !ClassUtils.isAssignable(BizTask.class, parameterTypes[0])) {
            throw new IllegalArgumentException("method: " + method.getDeclaringClass().getName() + "." + method.getName() + " 的只能有1个且必须是: com.weweibuy.framework.biztask.db.po.BizTask");
        }

    }


}
