package com.weweibuy.framework.common.feign.log;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import com.weweibuy.framework.common.feign.support.FeignLogSetting;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO 区分请求响应?
 *
 * @author durenhao
 * @date 2020/6/13 23:08
 **/
@Slf4j
public class FeignLogger extends Logger {

    private static Map<String, FeignLogSetting> configKeySetting = new ConcurrentHashMap<>(64);

    private static List<FeignLogSetting> feignLogSettingList = new ArrayList<>();

    public FeignLogger(List<FeignLogSetting> logSettingList) {
        feignLogSettingList = logSettingList;
    }

    public static void logForResponse(String body, Map<String, String> header, int status, long elapsedTime) {
        log.info("Feign 响应 Status: {}, Header: {}, Body: {}, 耗时: {}",
                status,
                JackJsonUtils.writeCamelCase(header),
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
        doLogRequest(request);
    }

    private static void doLogRequest(Request request) {
        FeignLogSetting feignLogSetting = configKeySetting(request);

        Map<String, Collection<String>> headers = request.headers();
        Map<String, String> header = reqHeader(headers, feignLogSetting);
        String bodyStr = reqBody(request, headers, feignLogSetting);
        log.info("Feign 请求地址: {}, Method: {}, Header: {}, Body: {}",
                request.url(),
                request.httpMethod(),
                JackJsonUtils.writeCamelCase(header),
                bodyStr);
    }

    @Override
    protected void logRetry(String configKey, Level logLevel) {
        // do nothing
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        return logAndReBufferResponse(response, elapsedTime);
    }

    @Override
    protected IOException logIOException(String configKey, Level logLevel, IOException ioe, long elapsedTime) {
        log.error("Feign 请求IO异常, 耗时: {}", elapsedTime, ioe);
        return ioe;
    }


    public static void logFilterRequest(Request request) {
        doLogRequest(request);
    }

    public static Response logAndReBufferFilterResponse(Request request, Response response, long startTime) throws IOException {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (response == null) {
            log.warn("Feign 响应为空, 耗时: {}",
                    elapsedTime);
            return null;
        }
        return logAndReBufferResponse(response, elapsedTime);
    }


    static Response logAndReBufferResponse(Response response, long elapsedTime) throws IOException {
        int status = response.status();

        FeignLogSetting feignLogSetting = configKeySetting(response.request());
        Map<String, Collection<String>> headers = response.headers();

        Map<String, String> header = respHeader(headers, feignLogSetting);

        Object[] objects = respBody(response, headers, feignLogSetting);

        logForResponse((String) objects[0], header, status, elapsedTime);
        return (Response) objects[1];
    }

    private static FeignLogSetting configKeySetting(Request request) {
        String configKey = request.requestTemplate()
                .methodMetadata().configKey();
        return configKeySetting.computeIfAbsent(configKey, key -> matching(request));
    }

    private static FeignLogSetting matching(Request request) {
        return feignLogSettingList.stream()
                .filter(s -> s.match(request))
                .findFirst()
                .orElse(FeignLogSetting.getDEFAULT());
    }

    private static Map<String, String> reqHeader(Map<String, Collection<String>> headers, FeignLogSetting feignLogSetting) {
        List<String> reqHeaderList = feignLogSetting.getReqHeaderList();
        if (CollectionUtils.isEmpty(reqHeaderList)) {
            return Collections.emptyMap();
        }
        return header(reqHeaderList, headers);
    }

    public static String reqBody(Request request, Map<String, Collection<String>> header, FeignLogSetting feignLogSetting) {
        Boolean disableResp = feignLogSetting.getDisableReqBody();
        if (disableResp != null && disableResp) {
            return StringUtils.EMPTY;
        }
        Collection<String> contentType = header.get(HttpHeaders.CONTENT_TYPE);
        boolean match = CollectionUtils.isNotEmpty(contentType) && contentType.stream()
                .anyMatch(c -> c.indexOf(MediaType.MULTIPART_FORM_DATA_VALUE) != -1);
        return match ? HttpRequestUtils.BOUNDARY_BODY : Optional.ofNullable(request.body())
                .map(b -> new String(b, CommonConstant.CharsetConstant.UT8))
                .orElse(StringUtils.EMPTY);
    }


    private static Map<String, String> respHeader(Map<String, Collection<String>> headers, FeignLogSetting feignLogSetting) {
        List<String> reqHeaderList = feignLogSetting.getRespHeaderList();
        if (CollectionUtils.isEmpty(reqHeaderList)) {
            return Collections.emptyMap();
        }
        return header(reqHeaderList, headers);
    }

    private static Map<String, String> header(List<String> reqHeaderList, Map<String, Collection<String>> headers) {
        Map<String, String> hashMap = new HashMap<>();
        reqHeaderList.forEach(s -> hashMap.put(s, Optional.ofNullable(headers.get(s))
                .filter(CollectionUtils::isNotEmpty)
                .map(l -> l.iterator().next())
                .orElse("")));
        return hashMap;
    }

    public static Object[] respBody(Response response, Map<String, Collection<String>> header, FeignLogSetting feignLogSetting) throws IOException {
        Boolean disableResp = feignLogSetting.getDisableRespBody();
        if (disableResp != null && disableResp) {
            return new Object[]{StringUtils.EMPTY, response};
        }
        int status = response.status();
        Collection<String> collection = header.get(HttpHeaders.CONTENT_TYPE);
        if (CollectionUtils.isNotEmpty(collection) && !HttpRequestUtils.notBoundaryBody(collection.iterator().next())) {
            return new Object[]{HttpRequestUtils.BOUNDARY_BODY, response};
        }
        String bodyStr = "";
        Response.Body body = response.body();
        if (body != null && !(status == 204 || status == 205)) {
            byte[] bodyData = Util.toByteArray(body.asInputStream());
            bodyStr = new String(bodyData, CommonConstant.CharsetConstant.UT8);
            if (!body.isRepeatable()) {
                response = response.toBuilder().body(bodyData).build();
            }
        }
        return new Object[]{bodyStr, response};
    }


}
