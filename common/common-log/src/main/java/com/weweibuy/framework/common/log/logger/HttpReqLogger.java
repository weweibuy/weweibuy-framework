package com.weweibuy.framework.common.log.logger;

import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * http 请求日志输出
 *
 * @author durenhao
 * @date 2020/6/3 22:34
 **/
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpReqLogger {


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
        if (!HttpRequestUtils.isIncludePayload(request)) {
            return "";
        }
        if (HttpRequestUtils.isBoundaryBody(request.getContentType())) {
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
                    HttpRequestUtils.headerMapStr(headerMap),
                    body);
        }

    }


    static Map<String, String> headerMap(Set<String> headerKeyList, Function<String, String> getHeaderF) {
        return Optional.ofNullable(headerKeyList)
                .map(l -> l.stream()
                        .map(k -> Pair.of(k, getHeaderF.apply(k)))
                        .filter(p -> StringUtils.isNotBlank(p.getValue()))
                        .collect(Collectors.toMap(Pair::getLeft, Pair::getRight, (o, n) -> n)))
                .orElse(null);
    }

}
