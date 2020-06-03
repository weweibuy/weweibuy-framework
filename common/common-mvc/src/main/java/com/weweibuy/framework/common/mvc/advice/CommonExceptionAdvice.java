package com.weweibuy.framework.common.mvc.advice;

import com.weweibuy.framework.common.core.exception.BusinessException;
import com.weweibuy.framework.common.core.exception.SystemException;
import com.weweibuy.framework.common.core.model.dto.CommonCodeJsonResponse;
import com.weweibuy.framework.common.log.logger.HttpLogger;
import com.weweibuy.framework.common.log.utils.HttpRequestUtils;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * mvc 全局异常处理
 *
 * @author durenhao
 * @date 2020/3/2 17:39
 **/
@RestControllerAdvice
@Slf4j
public class CommonExceptionAdvice {

    @Autowired(required = false)
    private UnknownExceptionHandler unknownExceptionHandler;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(HttpServletRequest request, BusinessException e) throws IOException {
        if (HttpRequestUtils.isJsonRequest(request.getContentType())) {
            HttpLogger.logForJsonRequest(request);
        }
        log.warn("业务异常: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonCodeJsonResponse.response(e.getCodeAndMsg()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(HttpServletRequest request, MethodArgumentNotValidException e) throws IOException {
        if (HttpRequestUtils.isJsonRequest(request.getContentType())) {
            HttpLogger.logForJsonRequest(request);
        }

        log.warn("输入参数错误: {}", e.getMessage());
        String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonCodeJsonResponse.badRequestParam(defaultMessage));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(HttpServletRequest request, BindException e) throws IOException {
        if (HttpRequestUtils.isJsonRequest(request.getContentType())) {
            HttpLogger.logForJsonRequest(request);
        }
        log.warn("输入参数错误: {}", e.getMessage());
        String defaultMessage = e.getFieldError().getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonCodeJsonResponse.badRequestParam(defaultMessage));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(HttpServletRequest request, HttpMessageNotReadableException e) throws IOException {
        if (HttpRequestUtils.isJsonRequest(request.getContentType())) {
            HttpLogger.logForJsonRequest(request);
        }
        log.warn("输入参数格式错误: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonCodeJsonResponse.badRequestParam("输入参数格式错误"));
    }


    @ExceptionHandler(SystemException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(HttpServletRequest request, SystemException e) throws IOException {
        if (HttpRequestUtils.isJsonRequest(request.getContentType())) {
            HttpLogger.logForJsonRequest(request);
        }
        log.error("系统异常: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonCodeJsonResponse.response(e.getCodeAndMsg()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(HttpServletRequest request, HttpRequestMethodNotSupportedException e) throws IOException {
        if (HttpRequestUtils.isJsonRequest(request.getContentType())) {
            HttpLogger.logForJsonRequest(request);
        }
        log.warn("请求 HttpMethod 错误: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonCodeJsonResponse.badRequestParam("请求HttpMethod错误"));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(HttpServletRequest request, Exception e) throws IOException {
        if (HttpRequestUtils.isJsonRequest(request.getContentType())) {
            HttpLogger.logForJsonRequest(request);
        }
        if (unknownExceptionHandler != null) {
            return unknownExceptionHandler.handlerException(request, e);
        }
        log.error("未知异常: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonCodeJsonResponse.unknownException());
    }


}
