package com.weweibuy.framework.idempotent.core.aspect;

import com.weweibuy.framework.idempotent.core.support.IdempotentInfo;
import com.weweibuy.framework.idempotent.core.support.IdempotentInfoParser;
import com.weweibuy.framework.idempotent.core.support.IdempotentManager;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.Assert;

/**
 * 切面
 *
 * @author durenhao
 * @date 2020/3/31 14:53
 **/
public class IdempotentAspect implements MethodInterceptor {

    private IdempotentInfoParser idempotentKeyParser;

    public IdempotentAspect(IdempotentInfoParser idempotentKeyParser) {
        Assert.notNull(idempotentKeyParser, "idempotentKeyParser 不能为空");
        this.idempotentKeyParser = idempotentKeyParser;
    }

    /**
     * 通知
     *
     * @param methodInvocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        IdempotentInfo idempotentInfo = idempotentKeyParser.parseIdempotentInfo(methodInvocation);
        IdempotentManager idempotentManager = idempotentInfo.getIdempotentManager();
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
