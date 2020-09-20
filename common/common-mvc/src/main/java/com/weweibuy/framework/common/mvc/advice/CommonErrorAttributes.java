package com.weweibuy.framework.common.mvc.advice;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.model.eum.CommonErrorCodeEum;
import com.weweibuy.framework.common.core.support.SystemIdGetter;
import com.weweibuy.framework.common.core.utils.HttpRequestUtils;
import com.weweibuy.framework.common.log.logger.HttpLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2020/3/2 18:27
 **/
@Slf4j
public class CommonErrorAttributes extends DefaultErrorAttributes {

    private SystemIdGetter systemIdGetter;

    private final Map<String, Object> notFoundAttributes = new LinkedHashMap<>(4);

    private final Map<String, Object> unKnownAttributes = new LinkedHashMap<>(4);

    private final Map<String, Object> requestExceptionAttributes = new LinkedHashMap<>(4);


    public CommonErrorAttributes() {
        this(null);
    }

    public CommonErrorAttributes(SystemIdGetter systemIdGetter) {
        notFoundAttributes.put("code", Optional.ofNullable(systemIdGetter)
                .map(SystemIdGetter::getSystemId)
                .map(id -> id + CommonErrorCodeEum.NOT_FOUND.getCode())
                .orElse(CommonErrorCodeEum.NOT_FOUND.getCode()));
        notFoundAttributes.put("msg", CommonErrorCodeEum.NOT_FOUND.getMsg());
        unKnownAttributes.put("code", Optional.ofNullable(systemIdGetter)
                .map(SystemIdGetter::getSystemId)
                .map(id -> id + CommonErrorCodeEum.UNKNOWN_EXCEPTION.getCode())
                .orElse(CommonErrorCodeEum.UNKNOWN_EXCEPTION.getCode()));
        unKnownAttributes.put("msg", CommonErrorCodeEum.UNKNOWN_EXCEPTION.getMsg());
        requestExceptionAttributes.put("msg", "请求参数错误");
    }


    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {

        String contentType = HttpRequestUtils.getRequestAttribute(webRequest, CommonConstant.HttpServletConstant.REQUEST_CONTENT_TYPE);

        Integer status = HttpRequestUtils.getRequestAttribute(webRequest, "javax.servlet.error.status_code");

        String path = HttpRequestUtils.getRequestAttribute(webRequest, "javax.servlet.error.request_uri");

        String method = HttpRequestUtils.getRequestAttribute(webRequest, CommonConstant.HttpServletConstant.REQUEST_METHOD);

        Map<String, String[]> parameterMap = HttpRequestUtils.getRequestAttribute(webRequest, CommonConstant.HttpServletConstant.REQUEST_PARAMETER_MAP);

        if (HttpRequestUtils.isJsonRequest(contentType)) {
            HttpLogger.logForJsonRequest(path, method, parameterMap, StringUtils.EMPTY);
        }

        if (status == null) {
            return unKnownAttributes;
        }

        if (HttpStatus.NOT_FOUND.value() == status) {
            return notFoundAttributes;
        }

        if (HttpStatus.INTERNAL_SERVER_ERROR.value() <= status) {
            return unKnownAttributes;
        }

        if (HttpStatus.BAD_REQUEST.value() <= status && status < HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            requestExceptionAttributes.put("code", Optional.ofNullable(systemIdGetter)
                    .map(SystemIdGetter::getSystemId)
                    .map(id -> id + status)
                    .orElse(status + ""));
            unKnownAttributes.put("msg", CommonErrorCodeEum.UNKNOWN_EXCEPTION.getMsg());
            return requestExceptionAttributes;
        }
        return unKnownAttributes;
    }


}
