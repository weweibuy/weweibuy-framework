package com.weweibuy.framework.idempotent.core.exception;

/**
 * 幂等异常
 *
 * @author durenhao
 * @date 2020/6/19 20:28
 **/
public class IdempotentException extends RuntimeException {

    public IdempotentException() {
    }

    public IdempotentException(String message) {
        super(message);
    }

    public IdempotentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdempotentException(Throwable cause) {
        super(cause);
    }

    public IdempotentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
