package com.weweibuy.framework.common.core.model.dto;

import com.weweibuy.framework.common.core.model.ResponseCodeAndMsg;
import com.weweibuy.framework.common.core.model.eum.CommonHttpResponseEum;
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
public class CommonDataJsonResponse<T> extends CommonCodeJsonResponse {

    private T data;

    public CommonDataJsonResponse(ResponseCodeAndMsg codeAndMsg, T data) {
        super(codeAndMsg);
        this.data = data;
    }


    public static <T> CommonDataJsonResponse<T> success(T data) {
        return new CommonDataJsonResponse(CommonHttpResponseEum.SUCCESS, data);
    }


    public static <T> CommonDataJsonResponse<T> response(ResponseCodeAndMsg codeAndMsg, T data) {
        return new CommonDataJsonResponse(codeAndMsg, data);
    }

}
