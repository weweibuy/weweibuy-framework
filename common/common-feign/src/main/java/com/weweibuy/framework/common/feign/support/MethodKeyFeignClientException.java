package com.weweibuy.framework.common.feign.support;

import feign.Request;

/**
 * @author durenhao
 * @date 2020/7/6 15:42
 **/
public class MethodKeyFeignClientException extends MethodKeyFeignException {


    public MethodKeyFeignClientException(String methodKey, int status, String message, Request request, byte[] body) {
        super(methodKey, status, message, request, body);
    }


}
