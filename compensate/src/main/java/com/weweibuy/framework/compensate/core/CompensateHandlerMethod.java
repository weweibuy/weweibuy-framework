package com.weweibuy.framework.compensate.core;

import lombok.Builder;
import lombok.Data;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author durenhao
 * @date 2020/2/14 19:56
 **/
@Data
@Builder
public class CompensateHandlerMethod {

    private Object bean;

    private Method method;

    private Object recoverBean;

    private Method recoverMethod;


    public Object invoke(Object[] args) throws InvocationTargetException, IllegalAccessException {
        ReflectionUtils.makeAccessible(method);
        return method.invoke(bean, args);
    }

    public Object invokeRecover(Object[] args) throws InvocationTargetException, IllegalAccessException {
        ReflectionUtils.makeAccessible(method);
        return recoverMethod.invoke(recoverBean, args);
    }

    public boolean hasRecoverMethod() {
        return recoverBean != null && recoverMethod != null;
    }

}
