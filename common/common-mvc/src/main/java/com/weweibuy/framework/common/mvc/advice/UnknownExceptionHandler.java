package com.weweibuy.framework.common.mvc.advice;

import com.weweibuy.framework.common.core.model.dto.CommonCodeJsonResponse;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * http 未知异常处理
 *
 * @author durenhao
 * @date 2020/3/2 18:38
 **/
public interface UnknownExceptionHandler {

    /**
     * 处理MVC 捕获的未知异常
     *
     * @param request
     * @param e
     * @return
     */
    ResponseEntity<CommonCodeJsonResponse> handlerException(HttpServletRequest request, Exception e);

}
