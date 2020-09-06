package com.weweibuy.framework.compensate.core;

import com.weweibuy.framework.compensate.annotation.Compensate;
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
public class CompensateMethodRegister {

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

        CompensateHandlerMethod.CompensateHandlerMethodBuilder handlerMethodBuilder = CompensateHandlerMethod.builder()
                .bean(bean)
                .method(method);

        if (!StringUtils.isEmpty(recoverBeanName) && !StringUtils.isEmpty(recoverMethodName)) {
            Object recoverBean = applicationContext.getBean(recoverBeanName);
            Assert.notNull(recoverBean, bean.getClass().getCanonicalName() + method.getName() + " 补偿指定的 recoverBean  " + recoverBeanName + "不存在");
            Method[] declaredMethods = ReflectionUtils.getAllDeclaredMethods(recoverBean.getClass());
            boolean hashMethod = false;
            for (Method recoverMethod : declaredMethods) {
                if (recoverMethod.getName().equals(recoverMethodName)) {
                    handlerMethodBuilder.recoverMethod(BridgeMethodResolver.findBridgedMethod(recoverMethod));
                    hashMethod = true;
                    break;
                }
            }
            Assert.isTrue(hashMethod, bean.getClass().getCanonicalName() + method.getName() + " 补偿指定的 recoverMethod: " + recoverMethodName + " 不存在");
            handlerMethodBuilder.recoverBean(recoverBean);
        }
        compensateHandlerMethodMap.put(key, handlerMethodBuilder.build());
    }


    public CompensateHandlerMethod getCompensateHandlerMethod(String key) {
        return compensateHandlerMethodMap.get(key);
    }

}
