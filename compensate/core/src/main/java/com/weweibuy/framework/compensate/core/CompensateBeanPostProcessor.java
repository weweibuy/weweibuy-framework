package com.weweibuy.framework.compensate.core;

import com.weweibuy.framework.compensate.annotation.Compensate;
import com.weweibuy.framework.compensate.support.MethodArgsTypeHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodParameter;
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

    private MethodArgsTypeHolder methodArgsTypeHolder;

    public CompensateBeanPostProcessor(CompensateMethodRegister compensateMethodRegister) {
        this.compensateMethodRegister = compensateMethodRegister;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getMethods();
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        ReflectionUtils.doWithLocalMethods(bean.getClass(), method -> {
            Compensate annotation = method.getAnnotation(Compensate.class);
            if (annotation != null) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes != null || parameterTypes.length > 0) {
                    for (int i = 0; i < parameterTypes.length; i++) {
                        MethodParameter methodParameter = new MethodParameter(method, i);
                        methodArgsTypeHolder.addType(generateKey(annotation.key(), i), methodParameter, parameterTypes[i]);
                    }
                }
                compensateMethodRegister.register(method, bean, annotation);
            }
        });
        return bean;
    }


    private String generateKey(String compensateKey, Integer argsIndex) {
        return compensateKey + "_" + argsIndex;
    }

    public void setMethodArgsTypeHolder(MethodArgsTypeHolder methodArgsTypeHolder) {
        this.methodArgsTypeHolder = methodArgsTypeHolder;
    }
}



