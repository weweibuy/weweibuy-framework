package com.weweibuy.framework.common.core.utils;

import com.weweibuy.framework.common.core.model.ResponseCodeAndMsg;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

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


    public static ResponseCodeAndMsg appendSystemId(String systemId, ResponseCodeAndMsg responseCodeAndMsg) {
        if (StringUtils.isBlank(systemId)) {
            return responseCodeAndMsg;
        }
        return newResponseCodeAndMsg(systemId + responseCodeAndMsg.getCode(), responseCodeAndMsg.getMsg());
    }

    public static ResponseCodeAndMsg appendSystemId(String systemId, String code, String msg) {
        if (StringUtils.isBlank(systemId)) {
            return newResponseCodeAndMsg(code, msg);
        }
        return newResponseCodeAndMsg(systemId + code, msg);
    }


}
