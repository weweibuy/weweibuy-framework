package com.weweibuy.framework.common.feign.log;

import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.entity.ByteArrayEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2022/10/15 11:58
 **/
@Slf4j
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class HttpClientLogger {

    private static String reqUrl(HttpRequest request) {
        String url = "";
        if (request instanceof HttpRequestWrapper) {
            HttpRequest original = ((HttpRequestWrapper) request).getOriginal();
            if (original instanceof HttpRequestBase) {
                URI uri = ((HttpRequestBase) original).getURI();
                url = uri.toString();
            } else {
                HttpHost target = ((HttpRequestWrapper) request).getTarget();
                URI uri = ((HttpRequestWrapper) request).getURI();
                url = target.toString() + uri.toString();
            }
        } else {
            url = request.getRequestLine().getUri();
        }
        return url;
    }

    public static void logReq(HttpRequest request) throws IOException {
        RequestLine requestLine = request.getRequestLine();
        String url = reqUrl(request);
        String contentType = Optional.ofNullable(request.getFirstHeader(HttpHeaders.CONTENT_TYPE))
                .map(Header::getValue)
                .orElse("");
        String body = "";
        if (StringUtils.isBlank(contentType)) {
            body = "";
        } else if (!HttpRequestUtils.contentTypeCanLogBody(contentType)) {
            // 上传文件
            body = HttpRequestUtils.BOUNDARY_BODY;
        } else if (request instanceof HttpEntityEnclosingRequest && ((HttpEntityEnclosingRequest) request).getEntity() != null) {
            HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest) request;
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            entity.writeTo(buffer);
            if (!entity.isRepeatable()) {
                entityRequest.setEntity(new ByteArrayEntity(buffer.toByteArray()));
            }
            body = new String(buffer.toByteArray());
        }

        log.info("Httpclient 请求地址: {}, Method: {}, Content-Type: {}, Body: {}",
                url,
                requestLine.getMethod(),
                contentType,
                body);
    }


    public static void logResp(HttpResponse response, Long reqTime) throws IOException {
        HttpEntity entity = response.getEntity();
        String body = "";

        String contentType = Optional.ofNullable(response.getFirstHeader(HttpHeaders.CONTENT_TYPE))
                .map(Header::getValue)
                .orElse("");
        if (!HttpRequestUtils.contentTypeCanLogBody(contentType)) {
            body = HttpRequestUtils.BOUNDARY_BODY;
        } else if (entity != null) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            entity.writeTo(buffer);
            if (!entity.isRepeatable()) {
                response.setEntity(new ByteArrayEntity(buffer.toByteArray()));
            }
            body = new String(buffer.toByteArray());
        }

        StatusLine statusLine = response.getStatusLine();
        log.info("Httpclient 响应 Status: {}, Content-Type: {}, Body: {}, 耗时: {}",
                statusLine.getStatusCode(),
                contentType,
                body,
                System.currentTimeMillis() - reqTime);
    }

}
