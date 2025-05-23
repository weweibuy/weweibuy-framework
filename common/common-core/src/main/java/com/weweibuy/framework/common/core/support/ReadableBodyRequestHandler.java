package com.weweibuy.framework.common.core.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 可读的http请求, 只在引入了 log 模块生效
 *
 * 可以处理例如: 对请求验签
 *
 * @author durenhao
 * @date 2021/11/15 23:41
 **/
public interface ReadableBodyRequestHandler {

    /**
     * 处理可读请求体的请求
     *
     * @param request
     * @return 是否继续执行下一个 heandler
     */
    boolean handlerReadableBodyRequest(HttpServletRequest request, HttpServletResponse response);

}
