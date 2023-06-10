package com.weweibuy.framework.common.mvc.advice;

import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.framework.common.core.model.eum.CommonErrorCodeEum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * 数据库异常处理
 *
 * @author durenhao
 * @date 2021/5/29 21:55
 **/
@Slf4j
@RestControllerAdvice
@Order(-200)
public class DBExceptionAdvice {

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<CommonCodeResponse> handler(HttpServletRequest request, DuplicateKeyException e) {
        log.error("数据重复输入: ", e);

        return CommonExceptionAdvice.builderCommonHeader(HttpStatus.BAD_REQUEST)
                .body(CommonCodeResponse.response(CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(), "数据重复输入"));
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CommonCodeResponse> handler(HttpServletRequest request, DataIntegrityViolationException e) {
        log.error("数据库操作异常:", e);

        String message = e.getMessage();
        if (message != null && message.indexOf("Data too long") != -1) {
            return CommonExceptionAdvice.builderCommonHeader(HttpStatus.BAD_REQUEST)
                    .body(CommonCodeResponse.response(CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(), "输入数据字段过长"));
        }
        Throwable cause = e.getCause();
        if (cause instanceof SQLException) {
            SQLException sqlException = (SQLException) cause;
            int errorCode = sqlException.getErrorCode();
            switch (errorCode) {
                case 1364:
                    return CommonExceptionAdvice.builderCommonHeader(HttpStatus.BAD_REQUEST)
                            .body(CommonCodeResponse.response(CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(), "数据操作异常,输入参数为空"));
            }
        }

        return CommonExceptionAdvice.builderCommonHeader(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonCodeResponse.response(CommonErrorCodeEum.UNKNOWN_SERVER_EXCEPTION.getCode(), "数据操作异常"));
    }

}
