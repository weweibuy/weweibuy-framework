package com.weweibuy.framework.common.feign.log;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * @author durenhao
 * @date 2020/6/13 23:08
 **/
@Slf4j
public class FeignLogger extends Logger {

    public static void logForRequest(String path, Map<String, Collection<String>> header, String body) {
        log.info("Feign 请求地址: {}, 请求头: {}, 请求数据: {}",
                path,
                header,
                body);
    }

    public static void logForResponse(Map<String, Collection<String>> header, String body, int status, long elapsedTime) {
        log.info("Feign 响应status: {} , 响应头: {}, 响应数据: {}, 耗时: {}",
                status,
                header,
                body,
                elapsedTime);


    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        // do nothing
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        log.info("Feign 请求地址: {}, 请求头: {}, 请求数据: {}",
                request.url(),
                request.headers(),
                request.requestBody().asString());
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
        if (response.body() != null && !(status == 204 || status == 205)) {
            byte[] bodyData = Util.toByteArray(response.body().asInputStream());
            bodyStr = new String(bodyData);
            response = response.toBuilder().body(bodyData).build();
        }
        logForResponse(response.headers(), bodyStr, status, elapsedTime);
        return response;
    }

}
