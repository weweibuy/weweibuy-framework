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

    SUCCESS("0", "请求成功"),

    BAD_REQUEST_PARAM("400", "请求参数错误"),

    REQUEST_EXCEPTION("400", "请求异常"),

    UNAUTHORIZED("401", "认证失败"),

    FORBIDDEN("403", "没有权限"),

    NOT_FOUND("404", "请求地址或数据不存在"),

    PAYLOAD_TOO_LARGE("413", "上传文件或请求报文过大"),

    UNSUPPORTED_MEDIA_TYPE("415", "不支持的请求类型"),

    TOO_MANY_REQUESTS("429", "请求限流"),

    UNKNOWN_SERVER_EXCEPTION("500", "服务异常"),

    JSON_PARSE_EXCEPTION("1000001", "Json数据解析异常"),

    JSON_WRITE_EXCEPTION("1000002", "Json序列化异常"),

    BAD_SYSTEM_REQUEST_PARAM("1000003", "系统级输入参数错误"),

    TOKEN_INVALID("1000004", "token失效"),

    BAD_SIGNATURE("1000005", "请求签名错误"),

    UNKNOWN_EXCEPTION("9999999", "未知异常"),

    NETWORK_EXCEPTION("9999998", "网络超时"),


    ;

    private String code;

    private String msg;

    CommonErrorCodeEum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
