package com.weweibuy.framework.common.feign.support;

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
     * {@link Feign#configKey(java.lang.Class, java.lang.reflect.Method)}
     */
    private String methodKey;


    public MethodKeyFeignException(String methodKey, int status, String message, Request request, byte[] body) {
        super(status, message, request, body);
        this.methodKey = methodKey;
    }

    public String getMethodKey() {
        return methodKey;
    }

}
