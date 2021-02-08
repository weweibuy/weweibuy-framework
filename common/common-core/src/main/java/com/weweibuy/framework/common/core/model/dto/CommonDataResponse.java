package com.weweibuy.framework.common.core.model.dto;

import com.weweibuy.framework.common.core.model.ResponseCodeAndMsg;
import com.weweibuy.framework.common.core.model.eum.CommonErrorCodeEum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author durenhao
 * @date 2019/5/12 22:30
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonDataResponse<T> extends CommonCodeResponse {

    private T data;

    public CommonDataResponse(ResponseCodeAndMsg codeAndMsg, T data) {
        super(codeAndMsg);
        this.data = data;
    }

    public CommonDataResponse(String code, String msg, T data) {
        super(code, msg);
        this.data = data;
    }


    public static <T> CommonDataResponse<T> success(T data) {
        return new CommonDataResponse(CommonErrorCodeEum.SUCCESS, data);
    }


    public static <T> CommonDataResponse<T> response(ResponseCodeAndMsg codeAndMsg, T data) {
        return new CommonDataResponse(codeAndMsg, data);
    }

    public static <T> CommonDataResponse<T> responseCoeMsg(String code, String msg) {
        return new CommonDataResponse(code, msg, null);
    }

    public static <T> CommonDataResponse<T> responseCoeMsg(String code, String msg, T data) {
        return new CommonDataResponse(code, msg, data);
    }

    public static <T> CommonDataResponse<T> responseCoeMsg(ResponseCodeAndMsg codeAndMsg, String msg) {
        return new CommonDataResponse(codeAndMsg.getCode(), msg, null);
    }

    public static <T> CommonDataResponse<T> responseCoeMsg(ResponseCodeAndMsg codeAndMsg, String msg, T data) {
        return new CommonDataResponse(codeAndMsg.getCode(), msg, data);
    }

    public static <T> CommonDataResponse<T> responseNoData(ResponseCodeAndMsg codeAndMsg) {
        return new CommonDataResponse(codeAndMsg, null);
    }

}
