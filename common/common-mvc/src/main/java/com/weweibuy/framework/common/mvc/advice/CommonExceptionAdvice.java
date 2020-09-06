package com.weweibuy.framework.common.mvc.advice;

import com.weweibuy.framework.common.core.exception.BusinessException;
import com.weweibuy.framework.common.core.exception.IdempotentNoLockException;
import com.weweibuy.framework.common.core.exception.SystemException;
import com.weweibuy.framework.common.core.model.dto.CommonCodeJsonResponse;
import com.weweibuy.framework.common.core.model.eum.CommonErrorCodeEum;
import com.weweibuy.framework.common.core.support.SystemIdGetter;
import com.weweibuy.framework.common.core.utils.ResponseCodeUtils;
import com.weweibuy.framework.common.log.logger.HttpLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
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
public class CommonExceptionAdvice implements InitializingBean {

    @Autowired(required = false)
    private UnknownExceptionHandler unknownExceptionHandler;

    @Autowired(required = false)
    private SystemIdGetter systemIdGetter;

    private String systemId;

    /**
     * 业务异常
     *
     * @param request
     * @param e
     * @return
     * @throws IOException
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(HttpServletRequest request, BusinessException e) throws IOException {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.warn("业务异常: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonCodeJsonResponse.response(
                        ResponseCodeUtils.appendSystemId(systemId, e.getCodeAndMsg())));
    }


    /**
     * 参数验证异常
     *
     * @param request
     * @param e
     * @return
     * @throws IOException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(HttpServletRequest request, MethodArgumentNotValidException e) throws IOException {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.warn("输入参数错误: {}", e.getMessage());
        String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonCodeJsonResponse.response(
                        ResponseCodeUtils.appendSystemId(systemId, CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(), defaultMessage)));
    }

    /**
     * 参数验证异常
     *
     * @param request
     * @param e
     * @return
     * @throws IOException
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(HttpServletRequest request, BindException e) throws IOException {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.warn("输入参数错误: {}", e.getMessage());
        String defaultMessage = e.getFieldError().getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonCodeJsonResponse.response(
                        ResponseCodeUtils.appendSystemId(systemId, CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(), defaultMessage)));
    }

    /**
     * 无法读取请求报文
     *
     * @param request
     * @param e
     * @return
     * @throws IOException
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(HttpServletRequest request, HttpMessageNotReadableException e) throws IOException {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.warn("输入参数格式错误: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonCodeJsonResponse.response(
                        ResponseCodeUtils.appendSystemId(systemId, CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(), "输入参数格式错误")));
    }

    /**
     * 系统异常
     *
     * @param request
     * @param e
     * @return
     * @throws IOException
     */
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(HttpServletRequest request, SystemException e) throws IOException {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.error("系统异常: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonCodeJsonResponse.response(
                        ResponseCodeUtils.appendSystemId(systemId, e.getCodeAndMsg())));
    }

    /**
     * 方法不支持
     *
     * @param request
     * @param e
     * @return
     * @throws IOException
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(HttpServletRequest request, HttpRequestMethodNotSupportedException e) throws IOException {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.warn("请求 HttpMethod 错误: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonCodeJsonResponse.response(
                        ResponseCodeUtils.appendSystemId(systemId, CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(), "请求HttpMethod错误")));
    }

    /**
     * 幂等异常
     *
     * @param request
     * @param e
     * @return
     * @throws IOException
     */
    @ExceptionHandler(IdempotentNoLockException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(HttpServletRequest request, IdempotentNoLockException e) throws IOException {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.warn("幂等异常: ", e.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(CommonCodeJsonResponse.response(
                        ResponseCodeUtils.appendSystemId(systemId, CommonErrorCodeEum.TOO_MANY_REQUESTS)));
    }

    /**
     * 未知异常
     *
     * @param request
     * @param e
     * @return
     * @throws IOException
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(HttpServletRequest request, Exception e) throws IOException {
        HttpLogger.determineAndLogForJsonRequest(request);

        if (unknownExceptionHandler != null) {
            return unknownExceptionHandler.handlerException(request, e);
        }
        log.error("未知异常: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonCodeJsonResponse.response(
                        ResponseCodeUtils.appendSystemId(systemId, CommonErrorCodeEum.UNKNOWN_EXCEPTION)));
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (systemIdGetter != null) {
            systemId = systemIdGetter.getSystemId();
        } else {
            log.warn("请设置systemId,以便Http响应报文可以区分系统");
            systemId = StringUtils.EMPTY;
        }
    }
}
