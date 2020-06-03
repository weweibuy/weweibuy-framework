package com.weweibuy.framework.common.core.model.eum;


import com.weweibuy.framework.common.core.model.ResponseCodeAndMsg;

/**
 * Http 响应枚举, code由三位组成
 *
 * @author durenhao
 * @date 2019/5/12 22:27
 **/
public enum CommonHttpResponseEum implements ResponseCodeAndMsg {

    SUCCESS("200", "请求成功"),
    BAD_REQUEST_PARAM("400", "请求参数错误"),
    UNAUTHORIZED("401", "认证失败"),
    FORBIDDEN("403", "没有权限"),
    UNSUPPORTED_MEDIA_TYPE("415", "不支持的请求格式"),
    TOO_MANY_REQUESTS("429", "请求限流"),
    UNKNOWN_EXCEPTION("500", "系统异常"),

    BAD_SYSTEM_REQUEST_PARAM("101010101", "系统级输入参数错误"),
    ;

    private String code;

    private String msg;


    CommonHttpResponseEum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
