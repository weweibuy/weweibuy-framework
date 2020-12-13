package com.weweibuy.framework.common.core.exception;

import feign.Request;

/**
 * @author durenhao
 * @date 2020/7/6 15:42
 **/
public class MethodKeyFeignServerException extends MethodKeyFeignException {


    public MethodKeyFeignServerException(String methodKey, int status, String message,
                                         Request request, byte[] body, String systemId) {
        super(methodKey, status, message, request, body, systemId);
    }

}
