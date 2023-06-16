package com.weweibuy.framework.common.feign.log;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.framework.common.feign.config.HttpClientProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author durenhao
 * @date 2022/10/15 11:58
 **/
@Slf4j
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class HttpClientLogger {


    public static void logReq(HttpRequest request, URI url, String method, HttpClientProperties.LogHttpProperties logProperties) throws IOException {
        Set<String> headerSet = Optional.ofNullable(logProperties)
                .map(HttpClientProperties.LogHttpProperties::getLogReqHeader)
                .orElse(Collections.emptySet());

        Map<String, String> headerMap = logHeaderMap(request, headerSet);
        String contentType = Optional.ofNullable(headerMap.get(HttpHeaders.CONTENT_TYPE))
                .orElse("");

        String body = reqBodyAndReBuffer(request, contentType, logProperties);
        String headerStr = headerMapStr(headerMap);

        log.info("Httpclient 请求地址: {}, Method: {}, Header: {}, Body: {}",
                url,
                method,
                headerStr,
                body);
    }


    private static Map<String, String> logHeaderMap(HttpMessage httpMessage, Set<String> headerSet) {
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

        if (!HttpRequestUtils.notBoundaryBody(contentType)) {
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


    public static void logResp(HttpResponse response, HttpClientProperties.LogHttpProperties logProperties, Long reqTime) throws IOException {
        Set<String> headerList = Optional.ofNullable(logProperties)
                .map(HttpClientProperties.LogHttpProperties::getLogRespHeader)
                .orElse(Collections.emptySet());

        Map<String, String> headerMap = logHeaderMap(response, headerList);
        String contentType = Optional.ofNullable(headerMap.get(HttpHeaders.CONTENT_TYPE))
                .orElse("");

        String body = respBodyAndReBuffer(response, contentType, logProperties);
        String headerStr = headerMapStr(headerMap);

        StatusLine statusLine = response.getStatusLine();
        log.info("Httpclient 响应 Status: {}, Header: {}, Body: {}, 耗时: {}",
                statusLine.getStatusCode(),
                headerStr,
                body,
                reqTime);
    }

    private static String headerMapStr(Map<String, String> headerMap) {
        return headerMap.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(",", "[", "]"));
    }

    private static String respBodyAndReBuffer(HttpResponse response, String contentType, HttpClientProperties.LogHttpProperties logProperties) throws IOException {
        Boolean disableReqBody = Optional.ofNullable(logProperties)
                .map(HttpClientProperties.LogHttpProperties::getDisableRespBody)
                .orElse(false);
        if (disableReqBody) {
            return HttpRequestUtils.BOUNDARY_BODY;
        }

        if (!HttpRequestUtils.notBoundaryBody(contentType)) {
            return HttpRequestUtils.BOUNDARY_BODY;
        }
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            entity.writeTo(buffer);
            Charset charset = parseCharset(contentType);
            if (!entity.isRepeatable()) {
                response.setEntity(new ByteArrayEntity(buffer.toByteArray()));
            }
            return new String(buffer.toByteArray(), charset);
        }
        return "";
    }


    private static Charset parseCharset(String contentType) {
        if (contentType.indexOf("charset") == -1 ||
                contentType.indexOf("UTF-8") != -1 || contentType.indexOf("utf-8") != -1) {
            return CommonConstant.CharsetConstant.UT8;
        }
        if (contentType.indexOf("GB") != -1 || contentType.indexOf("gb") != -1) {
            return CommonConstant.CharsetConstant.GBK;
        }
        try {
            ContentType parse = ContentType.parse(contentType);
            return Optional.ofNullable(parse.getCharset())
                    .orElse(CommonConstant.CharsetConstant.UT8);
        } catch (Exception e) {
            return CommonConstant.CharsetConstant.UT8;
        }
    }


}
