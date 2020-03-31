package com.weweibuy.framework.idempotent.core.aspect;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 切面
 *
 * @author durenhao
 * @date 2020/3/31 14:53
 **/
public class IdempotentAspect implements MethodInterceptor {


    /**
     * 通知
     *
     * @param methodInvocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        return null;
    }
}
