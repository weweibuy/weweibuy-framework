package com.weweibuy.framework.common.core.utils;

import com.weweibuy.framework.common.core.model.ResponseCodeAndMsg;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author durenhao
 * @date 2020/7/6 17:11
 **/
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ResponseCodeUtils {


    public static ResponseCodeAndMsg newResponseCodeAndMsg(String code, String msg) {
        return new ResponseCodeAndMsg() {
            @Override
            public String getCode() {
                return code;
            }

            @Override
            public String getMsg() {
                return msg;
            }
        };
    }

}
