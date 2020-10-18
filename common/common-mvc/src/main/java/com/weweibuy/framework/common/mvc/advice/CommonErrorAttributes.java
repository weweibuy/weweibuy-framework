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

    private final Map<String, Object> unKnownAttributes = new LinkedHashMap<>(4);


    public CommonErrorAttributes() {
        this(null);
    }

    public CommonErrorAttributes(SystemIdGetter systemIdGetter) {
        this.systemIdGetter = systemIdGetter;
        unKnownAttributes.put(CommonConstant.HttpResponseConstant.RESPONSE_MESSAGE_FIELD_CODE, Optional.ofNullable(systemIdGetter)
                .map(SystemIdGetter::getSystemId)
                .map(id -> id + CommonErrorCodeEum.UNKNOWN_SERVER_EXCEPTION.getCode())
                .orElse(CommonErrorCodeEum.UNKNOWN_SERVER_EXCEPTION.getCode()));
        unKnownAttributes.put(CommonConstant.HttpResponseConstant.RESPONSE_MESSAGE_FIELD_MSG, CommonErrorCodeEum.UNKNOWN_SERVER_EXCEPTION.getMsg());
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
        return getErrorAttributes(status);
    }


    private Map<String, Object> getErrorAttributes(Integer status) {
        String code = null;
        String msg = null;
        Map<String, Object> errorAttributeMap = new LinkedHashMap<>(4);
        switch (status) {
            case 400:
                code = CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode();
                msg = CommonErrorCodeEum.BAD_REQUEST_PARAM.getMsg();
                break;
            case 401:
                code = CommonErrorCodeEum.UNAUTHORIZED.getCode();
                msg = CommonErrorCodeEum.UNAUTHORIZED.getMsg();
                break;
            case 403:
                code = CommonErrorCodeEum.FORBIDDEN.getCode();
                msg = CommonErrorCodeEum.FORBIDDEN.getMsg();
                break;
            case 404:
                code = CommonErrorCodeEum.NOT_FOUND.getCode();
                msg = CommonErrorCodeEum.NOT_FOUND.getMsg();
                break;
            case 415:
                code = CommonErrorCodeEum.UNSUPPORTED_MEDIA_TYPE.getCode();
                msg = CommonErrorCodeEum.UNSUPPORTED_MEDIA_TYPE.getMsg();
                break;
            case 429:
                code = CommonErrorCodeEum.TOO_MANY_REQUESTS.getCode();
                msg = CommonErrorCodeEum.TOO_MANY_REQUESTS.getMsg();
                break;
            case 500:
                code = CommonErrorCodeEum.UNKNOWN_SERVER_EXCEPTION.getCode();
                msg = CommonErrorCodeEum.UNKNOWN_SERVER_EXCEPTION.getMsg();
                break;
            default:

        }
        if (StringUtils.isNotBlank(code)) {
            if (systemIdGetter != null) {
                code = systemIdGetter.getSystemId() + code;
            }
            errorAttributeMap.put(CommonConstant.HttpResponseConstant.RESPONSE_MESSAGE_FIELD_CODE, code);
            errorAttributeMap.put(CommonConstant.HttpResponseConstant.RESPONSE_MESSAGE_FIELD_MSG, msg);
            return errorAttributeMap;
        }

        if (HttpStatus.BAD_REQUEST.value() <= status && status < HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            errorAttributeMap.put(CommonConstant.HttpResponseConstant.RESPONSE_MESSAGE_FIELD_CODE, Optional.ofNullable(systemIdGetter)
                    .map(SystemIdGetter::getSystemId)
                    .map(id -> id + status)
                    .orElse(status + ""));
            errorAttributeMap.put(CommonConstant.HttpResponseConstant.RESPONSE_MESSAGE_FIELD_MSG, CommonErrorCodeEum.REQUEST_EXCEPTION.getMsg());
            return errorAttributeMap;
        }
        errorAttributeMap.put(CommonConstant.HttpResponseConstant.RESPONSE_MESSAGE_FIELD_CODE, Optional.ofNullable(systemIdGetter)
                .map(SystemIdGetter::getSystemId)
                .map(id -> id + status)
                .orElse(status + ""));
        errorAttributeMap.put(CommonConstant.HttpResponseConstant.RESPONSE_MESSAGE_FIELD_MSG, CommonErrorCodeEum.UNKNOWN_SERVER_EXCEPTION.getMsg());
        return errorAttributeMap;
    }

}
