package com.weweibuy.framework.common.mvc.advice;

import com.weweibuy.framework.common.core.exception.BusinessException;
import com.weweibuy.framework.common.core.exception.CustomResponseStatusException;
import com.weweibuy.framework.common.core.exception.IdempotentNoLockException;
import com.weweibuy.framework.common.core.exception.SystemException;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.framework.common.core.model.eum.CommonErrorCodeEum;
import com.weweibuy.framework.common.core.support.SystemIdGetter;
import com.weweibuy.framework.common.log.logger.HttpLogger;
import com.weweibuy.framework.common.log.support.LogTraceContext;
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
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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

    @Autowired
    private SystemIdGetter systemIdGetter;

    static String systemId;

    /**
     * 业务异常
     *
     * @param request
     * @param e
     * @return
     * @throws IOException
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommonCodeResponse> handler(HttpServletRequest request, BusinessException e) throws IOException {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.warn("业务异常: ", e);
        return builderCommonHeader(HttpStatus.BAD_REQUEST)
                .body(CommonCodeResponse.response(e.getCodeAndMsg()));
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
    public ResponseEntity<CommonCodeResponse> handler(HttpServletRequest request, MethodArgumentNotValidException e) throws IOException {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.warn("输入参数错误: {}", e.getMessage());
        String defaultMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        return builderCommonHeader(HttpStatus.BAD_REQUEST)
                .body(CommonCodeResponse.response(CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(), defaultMessage));

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
    public ResponseEntity<CommonCodeResponse> handler(HttpServletRequest request, BindException e) throws IOException {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.warn("输入参数错误: {}", e.getMessage());
        String defaultMessage = e.getFieldError().getDefaultMessage();
        return builderCommonHeader(HttpStatus.BAD_REQUEST)
                .body(CommonCodeResponse.response(CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(), defaultMessage));
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
    public ResponseEntity<CommonCodeResponse> handler(HttpServletRequest request, HttpMessageNotReadableException e) throws IOException {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.warn("输入参数格式错误: {}", e.getMessage());
        return builderCommonHeader(HttpStatus.BAD_REQUEST)
                .body(CommonCodeResponse.response(CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(), "输入参数格式错误"));
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
    public ResponseEntity<CommonCodeResponse> handler(HttpServletRequest request, SystemException e) throws IOException {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.error("系统异常: ", e);
        return builderCommonHeader(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonCodeResponse.response(e.getCodeAndMsg()));
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
    public ResponseEntity<CommonCodeResponse> handler(HttpServletRequest request, HttpRequestMethodNotSupportedException e) throws IOException {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.warn("请求 HttpMethod 错误: {}", e.getMessage());
        return builderCommonHeader(HttpStatus.BAD_REQUEST)
                .body(CommonCodeResponse.response(CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(), "请求HttpMethod错误"));
    }

    /**
     * 缺少请求参数
     *
     * @param request
     * @param e
     * @return
     * @throws IOException
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CommonCodeResponse> handler(HttpServletRequest request, MissingServletRequestParameterException e) {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.warn("缺少请求参数: {}", e.getMessage());
        return builderCommonHeader(HttpStatus.BAD_REQUEST)
                .body(CommonCodeResponse.response(CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(), "缺少请求参数: " + e.getParameterName()));
    }

    /**
     * 请求数据过大
     *
     * @param request
     * @param e
     * @return
     * @throws IOException
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<CommonCodeResponse> handler(HttpServletRequest request, MaxUploadSizeExceededException e) {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.warn("请求数据过大, 请设置: spring.servlet.multipart.max-request-size 与 spring.servlet.multipart.max-file-size.", e);
        return builderCommonHeader(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(CommonCodeResponse.response(CommonErrorCodeEum.PAYLOAD_TOO_LARGE));
    }

    /**
     * HTTP ResponseStatusException
     *
     * @param request
     * @param e
     * @return
     * @throws IOException
     */
    @ExceptionHandler(CustomResponseStatusException.class)
    public ResponseEntity<CommonCodeResponse> handler(HttpServletRequest request, CustomResponseStatusException e) throws IOException {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.warn("请求异常: {}", e.getMessage());
        return builderCommonHeader(e.getStatus())
                .body(CommonCodeResponse.response(e.getCodeAndMsg()));
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
    public ResponseEntity<CommonCodeResponse> handler(HttpServletRequest request, IdempotentNoLockException e) throws IOException {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.warn("幂等异常: ", e.getMessage());
        return builderCommonHeader(HttpStatus.TOO_MANY_REQUESTS)
                .body(CommonCodeResponse.response(CommonErrorCodeEum.TOO_MANY_REQUESTS));
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
    public ResponseEntity<CommonCodeResponse> handler(HttpServletRequest request, Exception e) throws IOException {
        HttpLogger.determineAndLogForJsonRequest(request);

        if (unknownExceptionHandler != null) {
            return unknownExceptionHandler.handlerException(request, e);
        }
        log.error("未知异常: ", e);

        return builderCommonHeader(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonCodeResponse.response(CommonErrorCodeEum.UNKNOWN_EXCEPTION));
    }

    /**
     * 构建通用的 响应Header
     *
     * @param httpStatus
     * @return
     */
    static ResponseEntity.BodyBuilder builderCommonHeader(HttpStatus httpStatus) {
        return builderCommonHeader(httpStatus.value());
    }

    static ResponseEntity.BodyBuilder builderCommonHeader(int status) {
        return ResponseEntity.status(status)
                .header(CommonConstant.HttpResponseConstant.RESPONSE_HEADER_FIELD_SYSTEM_ID, systemId)
                .header(CommonConstant.LogTraceConstant.HTTP_TRACE_CODE_HEADER, LogTraceContext.getTraceCode().orElse(StringUtils.EMPTY));
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        systemId = systemIdGetter.getSystemId();
        if (StringUtils.isBlank(systemId)) {
            log.warn("请设置systemId,以便Http响应报文可以区分系统");
            systemId = StringUtils.EMPTY;
        }
    }
}
