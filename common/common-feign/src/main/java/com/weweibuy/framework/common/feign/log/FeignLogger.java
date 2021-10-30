package com.weweibuy.framework.common.feign.log;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

/**
 * TODO 区分请求响应?
 *
 * @author durenhao
 * @date 2020/6/13 23:08
 **/
@Slf4j
public class FeignLogger extends Logger {

    private static final String BINARY_BODY_STR = "Binary data";

    public static void logForResponse(String body, int status, long elapsedTime) {
        log.info("Feign 响应 Status: {}, Body: {}, 耗时: {}",
                status,
                body,
                elapsedTime);
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        // do nothing
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        /*
         * spring-boot 2.2.2 版本
         *
         * String bodyStr = request.requestBody().asString();
         * String bodyStr = BINARY_BODY_STR.equals(bodyStr) ? StringUtils.EMPTY : bodyStr
         *
         */
        Collection<String> contentType = request.headers().get(HttpHeaders.CONTENT_TYPE);
        boolean match = CollectionUtils.isNotEmpty(contentType) && contentType.stream()
                .anyMatch(c -> c.indexOf(MediaType.MULTIPART_FORM_DATA_VALUE) != -1);

        String bodyStr = match ? BINARY_BODY_STR : Optional.ofNullable(request.body())
                .map(String::new)
                .orElse(StringUtils.EMPTY);
        log.info("Feign 请求地址: {}, Method: {}, Body: {}",
                request.url(),
                request.httpMethod(),
                bodyStr);
    }

    @Override
    protected void logRetry(String configKey, Level logLevel) {
        // do nothing
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        return logAndRebufferResponse(response, elapsedTime);
    }

    @Override
    protected IOException logIOException(String configKey, Level logLevel, IOException ioe, long elapsedTime) {
        log.error("Feign 请求IO异常, 耗时: {}", elapsedTime, ioe);
        return ioe;
    }

    static Response logAndRebufferResponse(Response response, long elapsedTime) throws IOException {
        int status = response.status();

        String bodyStr = "";

        Collection<String> collection = response.headers().get(HttpHeaders.CONTENT_TYPE);
        if (CollectionUtils.isNotEmpty(collection) && collection.iterator().next().indexOf("stream") != -1) {
            logForResponse(BINARY_BODY_STR, status, elapsedTime);
            return response;
        }
        Response.Body body = response.body();
        if (body != null && !(status == 204 || status == 205)) {
            byte[] bodyData = Util.toByteArray(body.asInputStream());
            bodyStr = new String(bodyData);
            if (!body.isRepeatable()) {
                response = response.toBuilder().body(bodyData).build();
            }
        }
        logForResponse(bodyStr, status, elapsedTime);
        return response;
    }

}
