package com.weweibuy.framework.compensate.exception;

/**
 * @author durenhao
 * @date 2020/2/15 20:44
 **/
public class CompensateException extends RuntimeException {

    public CompensateException() {
    }

    public CompensateException(String message) {
        super(message);
    }

    public CompensateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompensateException(Throwable cause) {
        super(cause);
    }

    public CompensateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
