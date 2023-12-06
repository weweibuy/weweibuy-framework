package com.weweibuy.framework.common.log.logger;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * http 响应日志输出
 *
 * @author durenhao
 * @date 2020/6/3 22:34
 **/
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpRespLogger {


    public static void logResponseBody(ContentCachingResponseWrapper response, Set<String> headerKeyList, Boolean disableRespBody, Long reqTime) {
        String body = HttpRequestUtils.BOUNDARY_BODY;
        if (!Boolean.TRUE.equals(disableRespBody)) {
            body = respBody(response);
        }
        Map<String, String> headerMap = HttpReqLogger.headerMap(headerKeyList, response::getHeader);
        logResponseBody(body, response.getStatus(), headerMap, reqTime);
    }


    private static String respBody(ContentCachingResponseWrapper response) {
        String contentType = response.getContentType();
        if (!HttpRequestUtils.notBoundaryBody(contentType)) {
            return HttpRequestUtils.BOUNDARY_BODY;
        }
        InputStream contentInputStream = response.getContentInputStream();
        try {
            return IOUtils.toString(contentInputStream, CommonConstant.CharsetConstant.UT8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    private static void logResponseBody(String body, int status, Map<String, String> headerMap, Long timestamp) {
        if (headerMap == null) {
            log.info("Http响应 Status: {}, Body: {}, 请求耗时: {}",
                    status,
                    body,
                    System.currentTimeMillis() - timestamp);
        } else {
            log.info("Http响应 Status: {}, Header: {}, Body: {}, 请求耗时: {}",
                    status,
                    HttpRequestUtils.headerMapStr(headerMap),
                    body,
                    System.currentTimeMillis() - timestamp);
        }

    }




}
