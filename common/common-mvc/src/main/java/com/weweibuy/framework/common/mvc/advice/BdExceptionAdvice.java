package com.weweibuy.framework.common.mvc.advice;

import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.framework.common.core.model.eum.CommonErrorCodeEum;
import com.weweibuy.framework.common.log.logger.HttpLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 数据库异常处理
 *
 * @author durenhao
 * @date 2021/5/29 21:55
 **/
@Slf4j
@RestControllerAdvice
@Order(-200)
public class BdExceptionAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CommonCodeResponse> handler(HttpServletRequest request, DataIntegrityViolationException e) {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.warn("输入数据字段过长: {}", e.getMessage());

        return CommonExceptionAdvice.builderCommonHeader(HttpStatus.BAD_REQUEST)
                .body(CommonCodeResponse.response(CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(), "输入数据字段过长"));
    }

}
