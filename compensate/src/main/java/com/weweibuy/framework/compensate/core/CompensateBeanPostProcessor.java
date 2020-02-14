package com.weweibuy.framework.compensate.core;

import com.weweibuy.framework.compensate.annotation.Compensate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author durenhao
 * @date 2020/2/14 19:51
 **/
public class CompensateBeanPostProcessor implements BeanPostProcessor {

    private CompensateMethodRegister compensateMethodRegister;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getMethods();
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        ReflectionUtils.doWithLocalMethods(bean.getClass(), method -> {
            Compensate annotation = method.getAnnotation(Compensate.class);
            if (annotation != null) {
                compensateMethodRegister.register(method, bean, annotation);
            }
        });
        return bean;
    }
}
