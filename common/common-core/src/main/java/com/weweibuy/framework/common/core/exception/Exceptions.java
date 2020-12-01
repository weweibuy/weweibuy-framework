package com.weweibuy.framework.common.core.exception;

import com.weweibuy.framework.common.core.model.ResponseCodeAndMsg;
import com.weweibuy.framework.common.core.model.eum.CommonErrorCodeEum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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

    public static BusinessException badRequestParam() {
        return new BusinessException(CommonErrorCodeEum.BAD_REQUEST_PARAM);
    }

    public static BusinessException business(String code, String msg) {
        return new BusinessException(newResponseCodeAndMsg(code, msg));
    }

    public static BusinessException formatBusiness(String code, String format, String... args) {
        return new BusinessException(newResponseCodeAndMsg(code, String.format(format, args)));
    }

    public static BusinessException formatBusiness(String format, String... args) {
        return new BusinessException(newResponseCodeAndMsg(CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(),
                String.format(format, args)));
    }

    public static BusinessException business(String msg) {
        return new BusinessException(newResponseCodeAndMsg(CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(), msg));
    }


    public static SystemException system(ResponseCodeAndMsg responseCodeAndMsg) {
        return new SystemException(responseCodeAndMsg);
    }

    public static SystemException system(String msg) {
        return new SystemException(CommonErrorCodeEum.UNKNOWN_EXCEPTION.getCode(), msg);
    }

    public static SystemException formatSystem(String format, String... args) {
        return new SystemException(CommonErrorCodeEum.UNKNOWN_EXCEPTION.getCode(),
                String.format(format, args));
    }

    public static SystemException system(Throwable e) {
        return new SystemException(CommonErrorCodeEum.UNKNOWN_EXCEPTION.getCode(), e);
    }

    public static SystemException system(String msg, Throwable e) {
        return new SystemException(msg, e);
    }

    public static SystemException formatSystem(Throwable e, String format, String... args) {
        return new SystemException(String.format(format, args), e);
    }

    public static SystemException system(ResponseCodeAndMsg responseCodeAndMsg, Throwable cause) {
        return new SystemException(responseCodeAndMsg, cause);
    }

    public static SystemException system(String code, String msg) {
        return new SystemException(newResponseCodeAndMsg(code, msg));
    }

    public static SystemException formatSystem(String code, String format, String... args) {
        return new SystemException(newResponseCodeAndMsg(code, String.format(format, args)));
    }


    public static SystemException unknown() {
        return new SystemException(CommonErrorCodeEum.UNKNOWN_EXCEPTION);
    }


}
