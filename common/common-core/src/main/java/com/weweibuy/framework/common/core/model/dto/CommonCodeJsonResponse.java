package com.weweibuy.framework.common.core.model.dto;

import com.weweibuy.framework.common.core.model.ResponseCodeAndMsg;
import com.weweibuy.framework.common.core.model.eum.CommonErrorCodeEum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author durenhao
 * @date 2019/5/12 22:19
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonCodeJsonResponse implements ResponseCodeAndMsg {

    private String code;

    private String msg;

    public CommonCodeJsonResponse(ResponseCodeAndMsg responseCodeAndMsg) {
        this.code = responseCodeAndMsg.getCode();
        this.msg = responseCodeAndMsg.getMsg();
    }

    public static CommonCodeJsonResponse success() {
        return new CommonCodeJsonResponse(CommonErrorCodeEum.SUCCESS);
    }

    public static CommonCodeJsonResponse unknownException() {
        return new CommonCodeJsonResponse(CommonErrorCodeEum.UNKNOWN_EXCEPTION);
    }

    public static CommonCodeJsonResponse requestLimit() {
        return new CommonCodeJsonResponse(CommonErrorCodeEum.TOO_MANY_REQUESTS);
    }

    public static CommonCodeJsonResponse badRequestParam() {
        return new CommonCodeJsonResponse(CommonErrorCodeEum.BAD_REQUEST_PARAM);
    }

    public static CommonCodeJsonResponse unSupportedMediaType() {
        return new CommonCodeJsonResponse(CommonErrorCodeEum.UNSUPPORTED_MEDIA_TYPE);
    }

    public static CommonCodeJsonResponse badRequestParam(String msg) {
        return new CommonCodeJsonResponse(CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(), msg);
    }

    public static CommonCodeJsonResponse badSystemRequestParam() {
        return new CommonCodeJsonResponse(CommonErrorCodeEum.BAD_SYSTEM_REQUEST_PARAM);
    }

    public static CommonCodeJsonResponse unauthorized() {
        return new CommonCodeJsonResponse(CommonErrorCodeEum.UNAUTHORIZED);
    }

    public static CommonCodeJsonResponse forbidden() {
        return new CommonCodeJsonResponse(CommonErrorCodeEum.FORBIDDEN);
    }

    public static CommonCodeJsonResponse response(String code, String msg) {
        return new CommonCodeJsonResponse(code, msg);
    }

    public static CommonCodeJsonResponse response(ResponseCodeAndMsg responseCodeAndMsg) {
        return new CommonCodeJsonResponse(responseCodeAndMsg);
    }

}
