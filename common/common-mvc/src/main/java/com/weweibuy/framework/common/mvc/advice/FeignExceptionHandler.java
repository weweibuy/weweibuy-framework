package com.weweibuy.framework.common.mvc.advice;

import com.weweibuy.framework.common.core.exception.MethodKeyFeignException;
import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 全局异常处理 捕获FeignException处理
 *
 * @author durenhao
 * @date 2020/7/6 16:50
 **/
public interface FeignExceptionHandler {

    /**
     * 处理 feignException
     *
     * @param request
     * @param e
     * @return
     */
    ResponseEntity<CommonCodeResponse> handlerException(HttpServletRequest request, MethodKeyFeignException e);

}
