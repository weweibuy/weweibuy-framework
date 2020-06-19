package com.weweibuy.framework.idempotent.redis;

import com.weweibuy.framework.idempotent.core.support.IdempotentInfo;
import com.weweibuy.framework.idempotent.core.support.IdempotentManager;

/**
 * @author durenhao
 * @date 2020/6/20 0:03
 **/
public class RedisIdempotentManager implements IdempotentManager {

    @Override
    public boolean tryLock(IdempotentInfo idempotentInfo) {
        return false;
    }

    @Override
    public Object handlerNoLock(IdempotentInfo idempotentInfo) {
        return null;
    }

    @Override
    public void complete(IdempotentInfo idempotentInfo, Object result) {

    }
}
