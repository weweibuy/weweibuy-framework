package com.weweibuy.framework.common.core.exception;

import feign.Feign;
import feign.FeignException;
import feign.Request;

/**
 * @author durenhao
 * @date 2020/7/6 15:42
 **/
public class MethodKeyFeignException extends FeignException {

    /**
     * 方法的key
     * {@link Feign#configKey(Class, java.lang.reflect.Method)}
     */
    private final String methodKey;

    /**
     * 系统 id
     */
    private final String systemId;


    public MethodKeyFeignException(String methodKey, int status,
                                   String message, Request request, byte[] body, String systemId) {
        super(status, message, request, body);
        this.methodKey = methodKey;
        this.systemId = systemId;
    }

    public String getMethodKey() {
        return methodKey;
    }

    public String getSystemId() {
        return systemId;
    }
}
