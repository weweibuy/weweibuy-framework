package com.weiweibuy.framework.common.mvc.advice;

import com.weiweibuy.framework.common.core.model.dto.CommonCodeJsonResponse;
import org.springframework.http.ResponseEntity;

/**
 * @author durenhao
 * @date 2020/3/2 18:38
 **/
public interface UnknownExceptionHandler {

    /**
     * 处理MVC 捕获的未知异常
     *
     * @param e
     * @return
     */
    ResponseEntity<CommonCodeJsonResponse> handlerException(Exception e);

}
