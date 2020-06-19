package com.weweibuy.framework.idempotent.core.aspect;

import com.weweibuy.framework.idempotent.core.support.IdempotentInfo;
import com.weweibuy.framework.idempotent.core.support.IdempotentInfoParser;
import com.weweibuy.framework.idempotent.core.support.IdempotentManager;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 切面
 *
 * @author durenhao
 * @date 2020/3/31 14:53
 **/
public class IdempotentAspect implements MethodInterceptor {

    private IdempotentManager idempotentManager;

    private IdempotentInfoParser idempotentKeyParser;

    /**
     * 通知
     *
     * @param methodInvocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        Object[] arguments = methodInvocation.getArguments();
        Method method = methodInvocation.getMethod();
        IdempotentInfo idempotentInfo = idempotentKeyParser.parseKey(methodInvocation);
        boolean lock = idempotentManager.tryLock(idempotentInfo);
        if (!lock) {
            return idempotentManager.handlerNoLock(idempotentInfo);
        }
        Object proceed = null;
        try {
            proceed = methodInvocation.proceed();
        } finally {
            idempotentManager.complete(idempotentInfo, proceed);
        }
        return proceed;
    }
}
