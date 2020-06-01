package com.weweibuy.framework.common.mvc.advice;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author durenhao
 * @date 2020/3/2 18:27
 **/
public class CommonErrorAttributes extends DefaultErrorAttributes {

    private final Map<String, Object> notFoundAttributes = new LinkedHashMap<>(2);

    public CommonErrorAttributes() {
        notFoundAttributes.put("code","404");
        notFoundAttributes.put("msg", "请求地址不存在");
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Integer status = getAttributeStatus(webRequest, "javax.servlet.error.status_code");
        if (Objects.nonNull(status) && HttpStatus.NOT_FOUND.value() == status) {
            return notFoundAttributes;
        }

        return super.getErrorAttributes(webRequest, includeStackTrace);
    }

    @SuppressWarnings("unchecked")
    private <T> T getAttributeStatus(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }

}
