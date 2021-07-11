package com.weweibuy.framework.common.feign.support;

import com.weweibuy.framework.common.core.exception.MethodKeyFeignClientException;
import com.weweibuy.framework.common.core.exception.MethodKeyFeignException;
import com.weweibuy.framework.common.core.exception.MethodKeyFeignServerException;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.support.SystemIdGetter;
import feign.FeignException;
import feign.Response;
import feign.Target;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.Optional;

import static java.lang.String.format;

/**
 * @author durenhao
 * @date 2020/7/6 15:43
 **/
public class CustomFeignErrorDecoder extends ErrorDecoder.Default {

    private static String oriSystemId = "";

    public CustomFeignErrorDecoder(SystemIdGetter systemIdGetter) {
        oriSystemId = systemIdGetter.getSystemId();
    }

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

        String systemId = Optional.ofNullable(response.headers().get(CommonConstant.HttpResponseConstant.RESPONSE_HEADER_FIELD_SYSTEM_ID))
                .filter(CollectionUtils::isNotEmpty)
                .map(c -> c.iterator().next())
                // 获取默认的系统id -->  格式 feign 服务/url 名称
                .orElseGet(() -> defaultFeignSystemId(response));

        if (status >= 400 && status < 500) {
            return new MethodKeyFeignClientException(methodKey, status, message, response.request(), body, systemId);
        }
        if (status >= 500) {
            return new MethodKeyFeignServerException(methodKey, status, message, response.request(), body, systemId);
        }
        return new MethodKeyFeignException(methodKey, status, message, response.request(), body, systemId);
    }


    private String defaultFeignSystemId(Response response) {
        Target<?> target = response.request().requestTemplate().feignTarget();
        return defaultFeignSystemId(target);
    }


    public static String defaultFeignSystemId(Target<?> target) {
        String name = target.name();
        String url = target.url();
        return oriSystemId + " [Feign " + name + " " + url + "]";
    }
}
