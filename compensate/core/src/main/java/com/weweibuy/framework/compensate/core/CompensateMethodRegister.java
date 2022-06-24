package com.weweibuy.framework.compensate.core;

import com.weweibuy.framework.compensate.annotation.Compensate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 补偿方法注册器
 *
 * @author durenhao
 * @date 2020/2/14 19:58
 **/
public class CompensateMethodRegister implements SmartInitializingSingleton {

    private Map<String, CompensateHandlerMethod> compensateHandlerMethodMap = new HashMap<>();

    private ApplicationContext applicationContext;

    public CompensateMethodRegister(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public synchronized void register(Method method, Object bean, Compensate compensate) {
        String key = compensate.key();
        CompensateHandlerMethod handlerMethod = compensateHandlerMethodMap.get(key);
        Assert.isNull(handlerMethod, "补偿Key " + key + " 必须唯一");
        String recoverBeanName = compensate.recover().beanName();
        String recoverMethodName = compensate.recover().method();
        boolean async = compensate.recover().async();
        CompensateHandlerMethod.CompensateHandlerMethodBuilder handlerMethodBuilder = CompensateHandlerMethod.builder()
                .bean(bean)
                .method(method).recoverMethodName(recoverMethodName)
                .recoverBeanName(recoverBeanName)
                .asyncRecover(async);
        compensateHandlerMethodMap.put(key, handlerMethodBuilder.build());
    }

    private void recoverInfo(CompensateHandlerMethod compensateHandlerMethod) {
        String recoverBeanName = compensateHandlerMethod.getRecoverBeanName();
        String recoverMethodName = compensateHandlerMethod.getRecoverMethodName();
        if (!StringUtils.hasLength(recoverBeanName) && !StringUtils.hasLength(recoverMethodName)) {
            Object recoverBean = applicationContext.getBean(recoverBeanName);
            Method[] declaredMethods = ReflectionUtils.getAllDeclaredMethods(recoverBean.getClass());
            boolean hashMethod = false;
            for (Method recoverMethod : declaredMethods) {
                if (recoverMethod.getName().equals(recoverMethodName)) {
                    compensateHandlerMethod.setRecoverMethod(BridgeMethodResolver.findBridgedMethod(recoverMethod));
                    hashMethod = true;
                    break;
                }
            }
            compensateHandlerMethod.setRecoverBean(recoverBean);
        }

    }

    public CompensateHandlerMethod getCompensateHandlerMethod(String key) {
        return compensateHandlerMethodMap.get(key);
    }


    @Override
    public void afterSingletonsInstantiated() {
        compensateHandlerMethodMap.values().forEach(this::recoverInfo);
    }
}
