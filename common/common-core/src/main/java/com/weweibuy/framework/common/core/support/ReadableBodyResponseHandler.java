package com.weweibuy.framework.common.core.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 可读的http请求
 *
 * @author durenhao
 * @date 2021/11/15 23:41
 **/
public interface ReadableBodyResponseHandler {

    /**
     * 处理可读请求体的请求
     *
     * @param response
     */
    void handlerReadableBodyResponse(HttpServletRequest request, HttpServletResponse response) throws Exception;


}
