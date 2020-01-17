package com.weweibuy.framework.rocketmq.core.consumer;

import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author durenhao
 * @date 2020/1/6 23:14
 **/
public class RocketHandlerMethod {

    private HandlerMethodArgumentResolverComposite argumentResolverComposite;

    private Object bean;

    private Class<?> beanType;

    private Method method;

    private Method bridgedMethod;

    private MethodParameter[] methodParameters;

    public RocketHandlerMethod(Object bean, Method method, HandlerMethodArgumentResolverComposite argumentResolverComposite) {
        Assert.notNull(bean, "Bean is required");
        Assert.notNull(method, "Method is required");
        this.bean = bean;
        this.beanType = ClassUtils.getUserClass(bean);
        this.method = method;
        this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
        this.methodParameters = initMethodParameters();
    }

    public Object invoke(List<MessageExt> messageExtList, Object... providedArgs) throws Exception {
        Object[] args = getMethodArgumentValues(messageExtList, providedArgs);
        Object returnValue = doInvoke(args);
        return returnValue;
    }

    /**
     * 解析方法参数
     *
     * @param messageExtList
     * @param providedArgs
     * @return
     */
    private Object[] getMethodArgumentValues(List<MessageExt> messageExtList, Object[] providedArgs) {
        Object[] args = new Object[methodParameters.length];

        for (int i = 0; i < methodParameters.length; i++) {
            MethodParameter methodParameter = methodParameters[i];

            args[i] = findProvidedArgument(methodParameter, messageExtList, providedArgs);

            if (args[i] != null) {
                continue;
            }

            // 解析参数
            args[i] = argumentResolverComposite.getArgumentResolver(methodParameter)
                    .resolveArgument(methodParameter, messageExtList);

        }

        return args;
    }


    protected static Object findProvidedArgument(MethodParameter parameter, List<MessageExt> messageExtList, Object... providedArgs) {

        if (parameter.getParameterType().isInstance(messageExtList)) {
            return messageExtList;
        }

        if (!ObjectUtils.isEmpty(providedArgs)) {
            for (Object providedArg : providedArgs) {
                if (parameter.getParameterType().isInstance(providedArg)) {
                    return providedArg;
                }
            }
        }
        return null;
    }


    /**
     * 调用目标方法
     *
     * @param args
     * @return
     */
    private Object doInvoke(Object[] args) {
        ReflectionUtils.makeAccessible(bridgedMethod);
        try {
            return bridgedMethod.invoke(bean, args);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    private MethodParameter[] initMethodParameters() {
        int count = this.bridgedMethod.getParameterCount();
        MethodParameter[] result = new MethodParameter[count];
        for (int i = 0; i < count; i++) {
            SynthesizingMethodParameter parameter = new SynthesizingMethodParameter(this.bridgedMethod, i);
            parameter.withContainingClass(this.beanType);
            result[i] = parameter;
        }
        return result;
    }


}
