package com.weweibuy.framework.common.feign.log;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.framework.common.feign.config.HttpClientProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
import org.apache.hc.core5.http.message.StatusLine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.hc.core5.http.HttpResponse;

/**
 * @author durenhao
 * @date 2022/10/15 11:58
 **/
@Slf4j
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class HttpRespClientLogger {

    public static void logResp(HttpResponse response, HttpClientProperties.LogHttpProperties logProperties, Long reqTime) throws IOException {
        Set<String> headerList = Optional.ofNullable(logProperties)
                .map(HttpClientProperties.LogHttpProperties::getLogRespHeader)
                .orElse(Collections.emptySet());

        Map<String, String> headerMap = HttpReqClientLogger.logHeaderMap(response, headerList);
        String contentType = Optional.ofNullable(headerMap.get(HttpHeaders.CONTENT_TYPE))
                .orElse("");

        String body = respBodyAndReBuffer(response, contentType, logProperties);
        String headerStr = HttpRequestUtils.headerMapStr(headerMap);


        log.info("Httpclient 响应 Status: {}, Header: {}, Body: {}, 耗时: {}",
                response.getCode(),
                headerStr,
                body,
                reqTime);
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
        if (response instanceof BasicClassicHttpResponse basicClassicHttpResponse && basicClassicHttpResponse.getEntity() != null) {
            HttpEntity entity = basicClassicHttpResponse.getEntity();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            entity.writeTo(buffer);
            Charset charset = parseCharset(contentType);
            if (!entity.isRepeatable()) {
                basicClassicHttpResponse.setEntity(new ByteArrayEntity(buffer.toByteArray(),
                        Optional.ofNullable(entity.getContentType())
                                .map(ContentType::parse)
                                .orElse(null)));
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
