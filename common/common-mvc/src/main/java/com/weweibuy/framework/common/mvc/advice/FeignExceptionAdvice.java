package com.weweibuy.framework.common.mvc.advice;

import com.weweibuy.framework.common.core.exception.MethodKeyFeignException;
import com.weweibuy.framework.common.core.model.ResponseCodeAndMsg;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.framework.common.core.model.eum.CommonErrorCodeEum;
import com.weweibuy.framework.common.feign.support.CustomFeignErrorDecoder;
import com.weweibuy.framework.common.log.logger.HttpLogger;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@Order(-100)
public class FeignExceptionAdvice {

    @Autowired(required = false)
    private FeignExceptionHandler exceptionHandler;

    /**
     * Feign 调用异常 处理
     *
     * @param request
     * @param e
     * @return
     * @throws IOException
     */
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<CommonCodeResponse> handler(HttpServletRequest request, FeignException e) {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.warn("调用接口异常: ", e);
        Throwable cause = e.getCause();
        if (cause instanceof IOException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(CommonConstant.HttpResponseConstant.RESPONSE_HEADER_FIELD_SYSTEM_ID,
                            CustomFeignErrorDecoder.defaultFeignSystemId(e.request()))
                    .body(CommonCodeResponse.response(CommonErrorCodeEum.NETWORK_EXCEPTION));
        }
        if (exceptionHandler != null && e instanceof MethodKeyFeignException) {
            return exceptionHandler.handlerException(request, (MethodKeyFeignException) e);
        }

        ResponseCodeAndMsg codeAndMsg = null;
        if (e.status() >= 300 && e.status() < 500) {
            codeAndMsg = CommonErrorCodeEum.REQUEST_EXCEPTION;
        } else {
            codeAndMsg = CommonErrorCodeEum.UNKNOWN_SERVER_EXCEPTION;
        }

        ResponseEntity.BodyBuilder builder = CommonExceptionAdvice.builderCommonHeader(e.status());
        if (e.hasRequest()) {
            builder = builder.header(CommonConstant.HttpResponseConstant.RESPONSE_HEADER_FIELD_SYSTEM_ID,
                    CustomFeignErrorDecoder.defaultFeignSystemId(e.request()));
        }
        return builder.body(CommonCodeResponse.response(codeAndMsg));
    }


}
