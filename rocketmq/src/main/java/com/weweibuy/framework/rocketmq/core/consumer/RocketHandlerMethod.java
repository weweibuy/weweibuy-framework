package com.weweibuy.framework.rocketmq.core.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.TypeUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author durenhao
 * @date 2020/1/6 23:14
 **/
@Slf4j
public class RocketHandlerMethod {

    private HandlerMethodArgumentResolverComposite argumentResolverComposite;

    private Object bean;

    private Class<?> beanType;

    private Method method;

    private Method bridgedMethod;

    private MethodParameter[] methodParameters;

    private Integer batchMaxSize;


    public RocketHandlerMethod(MethodRocketListenerEndpoint endpoint) {
        this.bean = endpoint.getBean();
        this.beanType = ClassUtils.getUserClass(bean);
        this.method = endpoint.getMethod();
        this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(endpoint.getMethod());
        this.argumentResolverComposite = endpoint.getArgumentResolverComposite();
        this.batchMaxSize = endpoint.getConsumeMessageBatchMaxSize();
        this.methodParameters = initMethodParameters();
    }


    public Object invoke(Object message, Object... providedArgs) throws Exception {

        Object[] args = getMethodArgumentValues(message, providedArgs);
        Object returnValue = doInvoke(args);
        return returnValue;
    }


    /**
     * 解析方法参数
     *
     * @param message
     * @param providedArgs
     * @return
     */
    private Object[] getMethodArgumentValues(Object message, Object[] providedArgs) {
        Object[] args = new Object[methodParameters.length];

        for (int i = 0; i < methodParameters.length; i++) {
            MethodParameter methodParameter = methodParameters[i];

            args[i] = findProvidedArgument(methodParameter, message, providedArgs);

            if (args[i] != null) {
                continue;
            }

            HandlerMethodArgumentResolver argumentResolver = argumentResolverComposite.getArgumentResolver(methodParameter);

            if (argumentResolver == null) {
                log.warn("Method: {}, 第: {} 个参数, 无法找到匹配的参数处理器", methodParameter.getMethod(), i);
                continue;
            }
            // 解析参数
            args[i] = argumentResolver.resolveArgument(methodParameter, message);

        }

        return args;
    }


    protected Object findProvidedArgument(MethodParameter parameter, Object message, Object... providedArgs) {
        Class<?> parameterType = parameter.getParameterType();
        Type genericParameterType = parameter.getGenericParameterType();

        if (parameterType.isInstance(message) && genericParameterType instanceof ParameterizedType) {
            ParameterizedType parameterGenericParameterType = (ParameterizedType) parameter.getGenericParameterType();
            Type[] arguments = parameterGenericParameterType.getActualTypeArguments();
            if (!ObjectUtils.isEmpty(arguments) && TypeUtils.isAssignable(MessageExt.class, arguments[0])) {
                return message;
            }
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
            SynthesizingMethodParameter parameter = new RocketMethodParameter(this.bridgedMethod, i, batchMaxSize);
            parameter.withContainingClass(this.beanType);
            result[i] = parameter;
        }
        return result;
    }


}
