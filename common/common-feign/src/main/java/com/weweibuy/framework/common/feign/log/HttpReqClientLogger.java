package com.weweibuy.framework.common.feign.log;

import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.framework.common.feign.config.HttpClientProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.*;
import org.apache.http.entity.ByteArrayEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2022/10/15 11:58
 **/
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpReqClientLogger {


    public static void logReq(HttpRequest request, URI url, String method, HttpClientProperties.LogHttpProperties logProperties) throws IOException {
        Set<String> headerSet = Optional.ofNullable(logProperties)
                .map(HttpClientProperties.LogHttpProperties::getLogReqHeader)
                .orElse(Collections.emptySet());

        Map<String, String> headerMap = logHeaderMap(request, headerSet);
        String contentType = Optional.ofNullable(headerMap.get(HttpHeaders.CONTENT_TYPE))
                .orElse("");

        String body = reqBodyAndReBuffer(request, contentType, logProperties);
        String headerStr = HttpRequestUtils.headerMapStr(headerMap);

        log.info("Httpclient 请求地址: {}, Method: {}, Header: {}, Body: {}",
                url,
                method,
                headerStr,
                body);
    }


    static Map<String, String> logHeaderMap(HttpMessage httpMessage, Set<String> headerSet) {
        return Optional.ofNullable(httpMessage.getAllHeaders())
                .filter(ArrayUtils::isNotEmpty)
                .map(a -> Arrays.stream(a)
                        .filter(h -> headerSet.contains(h.getName()) || HttpHeaders.CONTENT_TYPE.equals(h.getName()))
                        .collect(Collectors.toMap(Header::getName, Header::getValue)))
                .orElse(Collections.emptyMap());
    }

    private static String reqBodyAndReBuffer(HttpRequest request, String contentType, HttpClientProperties.LogHttpProperties logProperties) throws IOException {
        Boolean disableReqBody = Optional.ofNullable(logProperties)
                .map(HttpClientProperties.LogHttpProperties::getDisableReqBody)
                .orElse(false);
        if (disableReqBody) {
            return HttpRequestUtils.BOUNDARY_BODY;
        }

        if (HttpRequestUtils.isBoundaryBody(contentType)) {
            // 上传文件
            return HttpRequestUtils.BOUNDARY_BODY;
        }
        if (request instanceof HttpEntityEnclosingRequest && ((HttpEntityEnclosingRequest) request).getEntity() != null) {
            HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest) request;
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            entity.writeTo(buffer);
            if (!entity.isRepeatable()) {
                entityRequest.setEntity(new ByteArrayEntity(buffer.toByteArray()));
            }
            return new String(buffer.toByteArray());
        }
        return "";
    }


}
