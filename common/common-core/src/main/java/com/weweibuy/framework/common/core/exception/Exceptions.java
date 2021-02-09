package com.weweibuy.framework.common.core.exception;

import com.weweibuy.framework.common.core.model.ResponseCodeAndMsg;
import com.weweibuy.framework.common.core.model.eum.CommonErrorCodeEum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.UncheckedIOException;

import static com.weweibuy.framework.common.core.utils.ResponseCodeUtils.newResponseCodeAndMsg;

/**
 * @author durenhao
 * @date 2020/2/29 20:31
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Exceptions {

    public static BusinessException business(ResponseCodeAndMsg responseCodeAndMsg) {
        return new BusinessException(responseCodeAndMsg);
    }

    public static BusinessException business(ResponseCodeAndMsg responseCodeAndMsg, Throwable e) {
        return new BusinessException(responseCodeAndMsg, e);
    }


    public static BusinessException badRequestParam() {
        return new BusinessException(CommonErrorCodeEum.BAD_REQUEST_PARAM);
    }

    public static BusinessException business(String code, String msg) {
        return new BusinessException(newResponseCodeAndMsg(code, msg));
    }

    public static BusinessException business(String code, String msg, Throwable e) {
        return new BusinessException(newResponseCodeAndMsg(code, msg), e);
    }

    public static BusinessException formatBusinessWithCode(String code, String format, Object... args) {
        return new BusinessException(newResponseCodeAndMsg(code, String.format(format, args)));
    }

    public static BusinessException formatBusinessWithCode(String code, String format, Throwable e, Object... args) {
        return new BusinessException(newResponseCodeAndMsg(code, String.format(format, args)), e);
    }

    public static BusinessException formatBusiness(String format, Object... args) {
        return new BusinessException(newResponseCodeAndMsg(CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(),
                String.format(format, args)));
    }

    public static BusinessException formatBusiness(String format, Throwable e, Object... args) {
        return new BusinessException(newResponseCodeAndMsg(CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(),
                String.format(format, args)), e);
    }

    public static BusinessException business(ResponseCodeAndMsg codeAndMsg, String msg) {
        return business(codeAndMsg.getCode(), msg);
    }

    public static BusinessException business(ResponseCodeAndMsg codeAndMsg, String msg, Throwable e) {
        return business(codeAndMsg.getCode(), msg);
    }


    public static BusinessException business(String msg) {
        return new BusinessException(newResponseCodeAndMsg(CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(), msg));
    }

    public static BusinessException business(String msg, Throwable e) {
        return new BusinessException(newResponseCodeAndMsg(CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(), msg), e);
    }


    public static SystemException system(ResponseCodeAndMsg responseCodeAndMsg) {
        return new SystemException(responseCodeAndMsg);
    }

    public static SystemException system(ResponseCodeAndMsg responseCodeAndMsg, String msg) {
        return new SystemException(responseCodeAndMsg.getCode(), msg);
    }

    public static SystemException system(String msg) {
        return new SystemException(CommonErrorCodeEum.UNKNOWN_EXCEPTION.getCode(), msg);
    }

    public static SystemException formatSystem(String format, Object... args) {
        return new SystemException(CommonErrorCodeEum.UNKNOWN_EXCEPTION.getCode(),
                String.format(format, args));
    }

    public static SystemException system(Throwable e) {
        return new SystemException(CommonErrorCodeEum.UNKNOWN_EXCEPTION.getCode(), e);
    }

    public static SystemException system(String msg, Throwable e) {
        return new SystemException(msg, e);
    }

    public static SystemException formatSystem(Throwable e, String format, Object... args) {
        return new SystemException(String.format(format, args), e);
    }

    public static SystemException system(ResponseCodeAndMsg responseCodeAndMsg, Throwable cause) {
        return new SystemException(responseCodeAndMsg, cause);
    }

    public static SystemException system(String code, String msg) {
        return new SystemException(newResponseCodeAndMsg(code, msg));
    }

    public static SystemException formatSystem(String code, String format, Object... args) {
        return new SystemException(newResponseCodeAndMsg(code, String.format(format, args)));
    }


    public static SystemException unknown() {
        return new SystemException(CommonErrorCodeEum.UNKNOWN_EXCEPTION);
    }

    public static UncheckedIOException uncheckedIO(IOException e) {
        return new UncheckedIOException(e);
    }

    public static UncheckedIOException uncheckedIO(String msg, IOException e) {
        return new UncheckedIOException(msg, e);
    }

    public static CustomResponseStatusException responseStatusException(HttpStatus httpStatus, ResponseCodeAndMsg responseCodeAndMsg) {
        return new CustomResponseStatusException(httpStatus, responseCodeAndMsg);
    }

    public static CustomResponseStatusException responseStatusException(HttpStatus httpStatus, ResponseCodeAndMsg responseCodeAndMsg, Throwable throwable) {
        return new CustomResponseStatusException(throwable, httpStatus, responseCodeAndMsg);
    }

}
