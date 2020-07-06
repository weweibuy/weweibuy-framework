package com.weweibuy.framework.common.core.exception;

/**
 * 幂等无法获取锁异常
 *
 * @author durenhao
 * @date 2020/6/19 20:28
 **/
public class IdempotentNoLockException extends RuntimeException {

    public IdempotentNoLockException() {
    }

    public IdempotentNoLockException(String message) {
        super(message);
    }

    public IdempotentNoLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdempotentNoLockException(Throwable cause) {
        super(cause);
    }

    public IdempotentNoLockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
