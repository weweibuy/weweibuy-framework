package com.weweibuy.framework.common.feign.support;

import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

import java.io.IOException;

import static java.lang.String.format;

/**
 * @author durenhao
 * @date 2020/7/6 15:43
 **/
public class CustomFeignErrorDecoder extends ErrorDecoder.Default {


    @Override
    public Exception decode(String methodKey, Response response) {
        return (FeignException) decode0(methodKey, response);
    }

    private FeignException decode0(String methodKey, Response response) {

        String message = format("status %s reading %s", response.status(), methodKey);

        byte[] body = {};
        try {
            if (response.body() != null) {
                body = Util.toByteArray(response.body().asInputStream());
            }
        } catch (IOException ignored) { // NOPMD
        }
        int status = response.status();
        if (status >= 400 && status < 500) {
            return new MethodKeyFeignClientException(methodKey, status, message, response.request(), body);
        }
        if (status >= 500) {
            return new MethodKeyFeignServerException(methodKey, status, message, response.request(), body);
        }
        return new MethodKeyFeignException(methodKey, status, message, response.request(), body);
    }

}
