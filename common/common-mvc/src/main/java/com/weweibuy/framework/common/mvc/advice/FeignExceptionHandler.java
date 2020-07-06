package com.weweibuy.framework.common.mvc.advice;

import com.weweibuy.framework.common.core.model.dto.CommonCodeJsonResponse;
import com.weweibuy.framework.common.feign.support.MethodKeyFeignException;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

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
    ResponseEntity<CommonCodeJsonResponse> handlerException(HttpServletRequest request, MethodKeyFeignException e);

}
