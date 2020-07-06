package com.weweibuy.framework.common.core.model.eum;

import com.weweibuy.framework.common.core.model.ResponseCodeAndMsg;
import lombok.Getter;

/**
 *
 * 通用性的错误
 *
 * @author durenhao
 * @date 2020/6/3 21:46
 **/
@Getter
public enum CommonErrorCodeEum implements ResponseCodeAndMsg {

    JSON_PARSE_EXCEPTION("100001", "Json数据解析异常"),

    JSON_WRITE_EXCEPTION("100002", "Json序列化异常"),

    UNKNOWN_EXCEPTION("999999", "未知异常"),

    NETWORK_EXCEPTION("999998", "网络超时"),


    ;

    private String code;

    private String msg;

    CommonErrorCodeEum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
