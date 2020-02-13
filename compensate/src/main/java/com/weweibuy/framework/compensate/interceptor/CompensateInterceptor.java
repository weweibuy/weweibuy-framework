package com.weweibuy.framework.compensate.interceptor;


import com.weweibuy.framework.compensate.annotation.Compensate;
import com.weweibuy.framework.compensate.core.CompensateInfo;
import com.weweibuy.framework.compensate.core.CompensateStore;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 补偿切面
 *
 * @author durenhao
 * @date 2020/2/13 21:06
 **/
public class CompensateInterceptor implements MethodInterceptor {

    private CompensateStore compensateStore;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            return methodInvocation.proceed();
        } catch (Exception e) {
            Compensate annotation = methodInvocation.getMethod().getAnnotation(Compensate.class);
            Object[] arguments = methodInvocation.getArguments();
            compensateStore.saveCompensateInfo(new CompensateInfo());
            throw e;
        }
    }
}
