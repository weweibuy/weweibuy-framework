package com.weweibuy.framework.idempotent.core.support;

/**
 * 幂等管理器
 *
 * @author durenhao
 * @date 2020/3/31 14:48
 **/
public interface IdempotentManager {

    /**
     * 抢占锁
     *
     * @param idempotentInfo
     */
    boolean tryLock(IdempotentInfo idempotentInfo);

    /**
     * 没有抢占到锁的处理逻辑
     *
     * @param idempotentInfo
     * @return
     */
    Object handlerNoLock(IdempotentInfo idempotentInfo);

    /**
     * 占用到锁,并业务处理完成
     *
     * @param idempotentInfo
     * @param result
     */
    void complete( IdempotentInfo idempotentInfo, Object result);


}
