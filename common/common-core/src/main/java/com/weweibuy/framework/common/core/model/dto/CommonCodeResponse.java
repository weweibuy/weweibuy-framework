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
public class CommonCodeResponse implements ResponseCodeAndMsg {

    private String code;

    private String msg;

    public CommonCodeResponse(ResponseCodeAndMsg responseCodeAndMsg) {
        this.code = responseCodeAndMsg.getCode();
        this.msg = responseCodeAndMsg.getMsg();
    }

    public static CommonCodeResponse success() {
        return new CommonCodeResponse(CommonErrorCodeEum.SUCCESS);
    }

    public static CommonCodeResponse unknownException() {
        return new CommonCodeResponse(CommonErrorCodeEum.UNKNOWN_EXCEPTION);
    }

    public static CommonCodeResponse requestLimit() {
        return new CommonCodeResponse(CommonErrorCodeEum.TOO_MANY_REQUESTS);
    }

    public static CommonCodeResponse badRequestParam() {
        return new CommonCodeResponse(CommonErrorCodeEum.BAD_REQUEST_PARAM);
    }

    public static CommonCodeResponse unSupportedMediaType() {
        return new CommonCodeResponse(CommonErrorCodeEum.UNSUPPORTED_MEDIA_TYPE);
    }

    public static CommonCodeResponse badRequestParam(String msg) {
        return new CommonCodeResponse(CommonErrorCodeEum.BAD_REQUEST_PARAM.getCode(), msg);
    }

    public static CommonCodeResponse badSystemRequestParam() {
        return new CommonCodeResponse(CommonErrorCodeEum.BAD_SYSTEM_REQUEST_PARAM);
    }

    public static CommonCodeResponse unauthorized() {
        return new CommonCodeResponse(CommonErrorCodeEum.UNAUTHORIZED);
    }

    public static CommonCodeResponse forbidden() {
        return new CommonCodeResponse(CommonErrorCodeEum.FORBIDDEN);
    }

    public static CommonCodeResponse response(String code, String msg) {
        return new CommonCodeResponse(code, msg);
    }

    public static CommonCodeResponse response(ResponseCodeAndMsg responseCodeAndMsg) {
        return new CommonCodeResponse(responseCodeAndMsg);
    }

}
