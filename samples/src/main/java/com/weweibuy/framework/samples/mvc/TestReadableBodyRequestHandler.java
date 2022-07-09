package com.weweibuy.framework.samples.mvc;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.support.ReadableBodyRequestHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author durenhao
 * @date 2022/7/9 10:18
 **/
public class TestReadableBodyRequestHandler implements ReadableBodyRequestHandler {
    @Override
    public void handlerReadableBodyRequest(HttpServletRequest request, HttpServletResponse response, boolean servletLevel) {
        throw Exceptions.business("测试异常");
    }
}
