package com.weweibuy.framework.common.log.logger;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * http 请求响应日志输出
 * TODO 区分请求响应?
 *
 * @author durenhao
 * @date 2020/6/3 22:34
 **/
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpLogger {


    public static void logForRequest(HttpServletRequest request, Set<String> headerKeyList, Boolean disableReqBody) {
        Map<String, String> headerMap = headerMap(headerKeyList, request::getHeader);
        String body = HttpRequestUtils.BOUNDARY_BODY;
        if (!Boolean.TRUE.equals(disableReqBody)) {
            body = reqBody(request);
        }
        logForRequest(request.getRequestURI(), request.getMethod(), request.getParameterMap(),
                headerMap, body);
    }

    private static String reqBody(HttpServletRequest request) {
        boolean includePayload = HttpRequestUtils.isIncludePayload(request);
        if (!includePayload) {
            return "";
        }
        String contentType = request.getContentType();
        if (isBoundaryBody(contentType)) {
            return HttpRequestUtils.BOUNDARY_BODY;
        }
        return HttpRequestUtils.readRequestBodyForJson(request);
    }

    private static void logForRequest(String path, String method, Map<String, String[]> parameterMap,
                                      Map<String, String> headerMap, String body) {
        parameterMap = Optional.ofNullable(parameterMap)
                .orElse(Collections.emptyMap());
        if (headerMap == null) {
            log.info("Http请求 Path: {}, Method: {}, Parameter: {}, Body: {}",
                    path,
                    method,
                    HttpRequestUtils.parameterMapToString(parameterMap),
                    body);
        } else {
            log.info("Http请求 Path: {}, Method: {}, Parameter: {}, Header: {}, Body: {}",
                    path,
                    method,
                    HttpRequestUtils.parameterMapToString(parameterMap),
                    headerStr(headerMap),
                    body);
        }

    }


    public static void logResponseBody(ContentCachingResponseWrapper response, Set<String> headerKeyList, Boolean disableRespBody) {
        String body = HttpRequestUtils.BOUNDARY_BODY;
        if (!Boolean.TRUE.equals(disableRespBody)) {
            body = respBody(response);
        }
        Map<String, String> headerMap = headerMap(headerKeyList, response::getHeader);
        logResponseBody(body, response.getStatus(), headerMap);
    }

    private static boolean isBoundaryBody(String contentType) {
        return StringUtils.isNotBlank(contentType)
                && !HttpRequestUtils.notBoundaryBody(contentType);
    }

    private static String respBody(ContentCachingResponseWrapper response) {
        String contentType = response.getContentType();
        if (isBoundaryBody(contentType)) {
            return HttpRequestUtils.BOUNDARY_BODY;
        }
        InputStream contentInputStream = response.getContentInputStream();
        try {
            return IOUtils.toString(contentInputStream, CommonConstant.CharsetConstant.UT8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public static void logResponseBody(String body, int status, Map<String, String> headerMap) {
        Long timestamp = HttpRequestUtils.getRequestAttribute(RequestContextHolder.getRequestAttributes(), CommonConstant.HttpServletConstant.REQUEST_TIMESTAMP);
        if (headerMap == null) {
            log.info("Http响应 Status: {}, Body: {}, 请求耗时: {}",
                    status,
                    body,
                    System.currentTimeMillis() - timestamp);
        } else {
            log.info("Http响应 Status: {}, Header: {}, Body: {}, 请求耗时: {}",
                    status,
                    headerStr(headerMap),
                    body,
                    System.currentTimeMillis() - timestamp);
        }

    }

    private static String headerStr(Map<String, String> headerMap) {
        return headerMap.entrySet().stream()
                .map(e -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining(","));
    }


    private static Map<String, String> headerMap(Set<String> headerKeyList, Function<String, String> getHeaderF) {
        return Optional.ofNullable(headerKeyList)
                .map(l -> l.stream()
                        .map(k -> Pair.of(k, getHeaderF.apply(k)))
                        .filter(p -> StringUtils.isNotBlank(p.getValue()))
                        .collect(Collectors.toMap(Pair::getLeft, Pair::getRight, (o, n) -> n)))
                .orElse(null);
    }

}
