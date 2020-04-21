package com.weiweibuy.framework.common.mvc.advice;

import com.weiweibuy.framework.common.core.exception.BusinessException;
import com.weiweibuy.framework.common.core.exception.SystemException;
import com.weiweibuy.framework.common.core.model.dto.CommonCodeJsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author durenhao
 * @date 2020/3/2 17:39
 **/
@RestControllerAdvice
@Slf4j
public class CommonExceptionAdvice {

    @Autowired(required = false)
    private UnknownExceptionHandler unknownExceptionHandler;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(BusinessException e) {
        log.warn("业务异常: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonCodeJsonResponse.response(e.getCodeAndMsg()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(MethodArgumentNotValidException e) {
        log.warn("输入参数错误: {}", e);
        String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonCodeJsonResponse.badRequestParam(defaultMessage));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(BindException e) {
        log.warn("输入参数错误: ", e);
        String defaultMessage = e.getFieldError().getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonCodeJsonResponse.badRequestParam(defaultMessage));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(HttpMessageNotReadableException e) {
        log.warn("输入参数格式错误:", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonCodeJsonResponse.badRequestParam("输入参数格式错误"));
    }


    @ExceptionHandler(SystemException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(SystemException e) {
        log.error("系统异常: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonCodeJsonResponse.response(e.getCodeAndMsg()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(HttpRequestMethodNotSupportedException e) {
        log.error("请求 HttpMethod 错误: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonCodeJsonResponse.badRequestParam("请求HttpMethod错误"));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(Exception e) {
        if (unknownExceptionHandler != null) {
            return unknownExceptionHandler.handlerException(e);
        }
        log.error("未知异常: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonCodeJsonResponse.unknownException());
    }


}
