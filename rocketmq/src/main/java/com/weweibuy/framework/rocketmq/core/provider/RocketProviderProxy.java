package com.weweibuy.framework.rocketmq.core.provider;


import com.weweibuy.framework.common.core.utils.MethodUtils;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author durenhao
 * @date 2019/12/29 21:04
 **/
public class RocketProviderProxy implements InvocationHandler {

    private final Map<Method, MethodHandler> methodMethodHandlerMap;

    public RocketProviderProxy(Map<Method, MethodHandler> methodMethodHandlerMap) {
        this.methodMethodHandlerMap = methodMethodHandlerMap;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else if (MethodUtils.isDefault(method)) {
            return invokeDefaultMethod(proxy, method, args);
        }
        return methodMethodHandlerMap.get(method).invoke(args);
    }



    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args)
            throws Throwable {
        final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
                .getDeclaredConstructor(Class.class, int.class);
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        final Class<?> declaringClass = method.getDeclaringClass();
        return constructor
                .newInstance(declaringClass,
                        MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED
                                | MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC)
                .unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args);
    }

}
