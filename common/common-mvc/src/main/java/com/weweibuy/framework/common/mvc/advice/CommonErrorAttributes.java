package com.weweibuy.framework.common.mvc.advice;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.log.utils.HttpRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author durenhao
 * @date 2020/3/2 18:27
 **/
@Slf4j
public class CommonErrorAttributes extends DefaultErrorAttributes {

    private final Map<String, Object> notFoundAttributes = new LinkedHashMap<>(2);

    public CommonErrorAttributes() {
        notFoundAttributes.put("code", "404");
        notFoundAttributes.put("msg", "请求地址不存在");
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Integer status = HttpRequestUtils.getRequestAttribute(webRequest, "javax.servlet.error.status_code");

        String contentType = HttpRequestUtils.getRequestAttribute(webRequest, CommonConstant.HttpServletConstant.REQUEST_CONTENT_TYPE);

        String path = HttpRequestUtils.getRequestAttribute(webRequest, "javax.servlet.error.request_uri");

        String method = HttpRequestUtils.getRequestAttribute(webRequest, CommonConstant.HttpServletConstant.REQUEST_METHOD);

        if (Objects.nonNull(status) && HttpStatus.NOT_FOUND.value() == status) {
            // Json 请求在过滤器里不会输出日志 在此输出
            if (HttpRequestUtils.isJsonRequest(contentType)) {
                log.info("请求路径: {}, Method: {} ", path, method);
            }
            return notFoundAttributes;
        }

        return super.getErrorAttributes(webRequest, includeStackTrace);
    }


}
