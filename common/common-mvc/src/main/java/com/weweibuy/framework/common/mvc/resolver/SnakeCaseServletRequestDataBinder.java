package com.weweibuy.framework.common.mvc.resolver;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.ServletRequest;

/**
 * @author durenhao
 * @date 2020/2/17 23:21
 **/
public class SnakeCaseServletRequestDataBinder extends ExtendedServletRequestDataBinder {


    public SnakeCaseServletRequestDataBinder(Object target) {
        super(target);
    }

    public SnakeCaseServletRequestDataBinder(Object target, String objectName) {
        super(target, objectName);
    }

    @Override
    public void bind(ServletRequest request) {
        MutablePropertyValues mpvs = new SnakeCaseMutablePropertyValues(request);
        MultipartRequest multipartRequest = WebUtils.getNativeRequest(request, MultipartRequest.class);
        if (multipartRequest != null) {
            bindMultipart(multipartRequest.getMultiFileMap(), mpvs);
        }
        addBindValues(mpvs, request);
        doBind(mpvs);
    }

}
