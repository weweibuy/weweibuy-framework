package com.weweibuy.framework.common.core.model.dto;

import com.weweibuy.framework.common.core.model.ResponseCodeAndMsg;
import com.weweibuy.framework.common.core.model.eum.CommonHttpResponseEum;
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
public class CommonCodeJsonResponse implements ResponseCodeAndMsg{

    private String code;

    private String msg;

    public CommonCodeJsonResponse(ResponseCodeAndMsg responseCodeAndMsg) {
        this.code = responseCodeAndMsg.getCode();
        this.msg = responseCodeAndMsg.getMsg();
    }

    public static final CommonCodeJsonResponse success() {
        return new CommonCodeJsonResponse(CommonHttpResponseEum.SUCCESS);
    }

    public static final CommonCodeJsonResponse unknownException() {
        return new CommonCodeJsonResponse(CommonHttpResponseEum.UNKNOWN_EXCEPTION);
    }

    public static final CommonCodeJsonResponse requestLimit() {
        return new CommonCodeJsonResponse(CommonHttpResponseEum.TOO_MANY_REQUESTS);
    }

    public static final CommonCodeJsonResponse badRequestParam() {
        return new CommonCodeJsonResponse(CommonHttpResponseEum.BAD_REQUEST_PARAM);
    }

    public static final CommonCodeJsonResponse unSupportedMediaType() {
        return new CommonCodeJsonResponse(CommonHttpResponseEum.UNSUPPORTED_MEDIA_TYPE);
    }

    public static final CommonCodeJsonResponse badRequestParam(String msg) {
        return new CommonCodeJsonResponse(CommonHttpResponseEum.BAD_REQUEST_PARAM.getCode(), msg);
    }

    public static final CommonCodeJsonResponse badSystemRequestParam() {
        return new CommonCodeJsonResponse(CommonHttpResponseEum.BAD_SYSTEM_REQUEST_PARAM);
    }

    public static final CommonCodeJsonResponse unauthorized() {
        return new CommonCodeJsonResponse(CommonHttpResponseEum.UNAUTHORIZED);
    }

    public static final CommonCodeJsonResponse forbidden() {
        return new CommonCodeJsonResponse(CommonHttpResponseEum.FORBIDDEN);
    }

    public static final CommonCodeJsonResponse response(String code, String msg) {
        return new CommonCodeJsonResponse(code, msg);
    }

    public static final CommonCodeJsonResponse response(ResponseCodeAndMsg responseCodeAndMsg) {
        return new CommonCodeJsonResponse(responseCodeAndMsg);
    }

}
