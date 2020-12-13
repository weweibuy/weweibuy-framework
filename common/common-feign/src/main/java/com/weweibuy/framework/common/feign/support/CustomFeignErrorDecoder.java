package com.weweibuy.framework.common.feign.support;

import com.weweibuy.framework.common.core.exception.MethodKeyFeignClientException;
import com.weweibuy.framework.common.core.exception.MethodKeyFeignException;
import com.weweibuy.framework.common.core.exception.MethodKeyFeignServerException;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import static java.lang.String.format;

/**
 * @author durenhao
 * @date 2020/7/6 15:43
 **/
public class CustomFeignErrorDecoder extends ErrorDecoder.Default {


    @Override
    public Exception decode(String methodKey, Response response) {
        return decode0(methodKey, response);
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
        Collection<String> systemIdCollection = response.headers().getOrDefault(CommonConstant.HttpResponseConstant.RESPONSE_HEADER_FIELD_SYSTEM_ID,
                Collections.singletonList(StringUtils.EMPTY));
        String systemId = systemIdCollection.iterator().next();

        if (status >= 400 && status < 500) {
            return new MethodKeyFeignClientException(methodKey, status, message, response.request(), body, systemId);
        }
        if (status >= 500) {
            return new MethodKeyFeignServerException(methodKey, status, message, response.request(), body, systemId);
        }
        return new MethodKeyFeignException(methodKey, status, message, response.request(), body, systemId);
    }

}
